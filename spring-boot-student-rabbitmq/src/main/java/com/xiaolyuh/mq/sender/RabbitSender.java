package com.xiaolyuh.mq.sender;

import com.alibaba.fastjson.JSON;
import com.xiaolyuh.config.SystemConfig;
import com.xiaolyuh.mq.CorrelationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Rabbit 发送消息
 *
 * @author yuhao.wang
 */
@Service
public class RabbitSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback, InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(RabbitSender.class);

    /**
     * Rabbit MQ 客户端
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 系统配置
     */
    @Autowired
    private SystemConfig systemConfig;

    /**
     * 发送MQ消息
     *
     * @param exchangeName 交换机名称
     * @param routingKey   路由名称
     * @param message      发送消息体
     */
    public void sendMessage(String exchangeName, String routingKey, Object message) {
        Assert.notNull(message, "message 消息体不能为NULL");
        Assert.notNull(exchangeName, "exchangeName 不能为NULL");
        Assert.notNull(routingKey, "routingKey 不能为NULL");

        // 获取CorrelationData对象
        CorrelationData correlationData = this.correlationData(message);

        logger.info("发送MQ消息，消息ID：{}，消息体:{}, exchangeName:{}, routingKey:{}",
                correlationData.getId(), JSON.toJSONString(message), exchangeName, routingKey);
        // 发送消息
        this.convertAndSend(exchangeName, routingKey, message, correlationData);
    }

    /**
     * 用于实现消息发送到RabbitMQ交换器后接收ack回调。
     * 如果消息发送确认失败就进行重试。
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(org.springframework.amqp.rabbit.support.CorrelationData correlationData, boolean ack, String cause) {
        // 消息回调确认失败处理
        if (!ack && correlationData instanceof CorrelationData) {
            CorrelationData correlationDataExtends = (CorrelationData) correlationData;

            //消息发送失败,就进行重试，重试过后还不能成功就记录到数据库
            if (correlationDataExtends.getRetryCount() < systemConfig.getMqRetryCount()) {
                logger.info("MQ消息发送失败，消息重发，消息ID：{}，重发次数：{}，消息体:{}", correlationDataExtends.getId(),
                        correlationDataExtends.getRetryCount(), JSON.toJSONString(correlationDataExtends.getMessage()));

                // 将重试次数加一
                correlationDataExtends.setRetryCount(correlationDataExtends.getRetryCount() + 1);

                // 重发发消息
                this.convertAndSend(correlationDataExtends.getExchange(), correlationDataExtends.getRoutingKey(),
                        correlationDataExtends.getMessage(), correlationDataExtends);
            } else {
                //消息重试发送失败,将消息放到数据库等待补发
                logger.warn("MQ消息重发失败，消息入库，消息ID：{}，消息体:{}", correlationData.getId(),
                        JSON.toJSONString(correlationDataExtends.getMessage()));

                // TODO 保存消息到数据库
            }
        } else {
            logger.info("消息发送成功,消息ID:{}", correlationData.getId());
        }
    }

    /**
     * 用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调。
     * 基本上来说线上不可能出现这种情况，除非手动将已经存在的队列删掉，否则在测试阶段肯定能测试出来。
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.error("MQ消息发送失败，replyCode:{}, replyText:{}，exchange:{}，routingKey:{}，消息体:{}",
                replyCode, replyText, exchange, routingKey, JSON.toJSONString(message.getBody()));

        // TODO 保存消息到数据库
    }

    /**
     * 消息相关数据（消息ID）
     *
     * @param message
     * @return
     */
    private CorrelationData correlationData(Object message) {

        return new CorrelationData(UUID.randomUUID().toString(), message);
    }

    /**
     * 发送消息
     *
     * @param exchange        交换机名称
     * @param routingKey      路由key
     * @param message         消息内容
     * @param correlationData 消息相关数据（消息ID）
     * @throws AmqpException
     */
    private void convertAndSend(String exchange, String routingKey, final Object message, CorrelationData correlationData) throws AmqpException {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
        } catch (Exception e) {
            logger.error("MQ消息发送异常，消息ID：{}，消息体:{}, exchangeName:{}, routingKey:{}",
                    correlationData.getId(), JSON.toJSONString(message), exchange, routingKey, e);

            // TODO 保存消息到数据库
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
}

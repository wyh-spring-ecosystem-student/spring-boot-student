package com.xiaolyuh;

import io.swagger.annotations.Api;
import javax.annotation.Resource;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产者
 *
 * @Author https://blog.csdn.net/c20611
 * @Date 2024/10/29 17:20
 */
@Api(tags = "消息服务端")
@RequestMapping("/producer")
@RestController
public class RocketMQProducerController {

    private static final Logger log = LoggerFactory.getLogger(RocketMQProducerController.class);
    @Resource
    private RocketMQClientTemplate rocketMQTemplate;

    /**
     * 同步消息
     */
    @RequestMapping("/ProducerMq1")
    public void ProducerMQ1() {
        String stringTopic = "test-topic:tag1";
        String payloadStr = "Hello, RocketMQ!";
        SendReceipt sendResult = rocketMQTemplate.syncSendNormalMessage(stringTopic, payloadStr);
        log.info("MQ同步发送:{},返回结果:{}", stringTopic, sendResult);
    }
}
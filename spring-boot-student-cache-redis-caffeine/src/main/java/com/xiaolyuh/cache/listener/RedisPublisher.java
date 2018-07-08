package com.xiaolyuh.cache.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * redis消息的发布者
 *
 * @author yuhao.wang
 */
public class RedisPublisher {
    private static final Logger logger = LoggerFactory.getLogger(RedisPublisher.class);

    RedisOperations<? extends Object, ? extends Object> redisOperations;

    /**
     * 频道名称
     */
    ChannelTopic channelTopic;

    /**
     * @param redisOperations Redis客户端
     * @param channelTopic    频道名称
     */
    public RedisPublisher(RedisOperations<? extends Object, ? extends Object> redisOperations, ChannelTopic channelTopic) {
        this.channelTopic = channelTopic;
        this.redisOperations = redisOperations;
    }

    /**
     * 发布消息到频道（Channel）
     *
     * @param message 消息内容
     */
    public void publisher(Object message) {
        redisOperations.convertAndSend(channelTopic.toString(), message);
        logger.info("redis消息发布者向频道【{}】发布了【{}】消息", channelTopic.toString(), message.toString());
    }
}

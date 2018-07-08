package com.xiaolyuh.cache.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * redis消息的发布者
 *
 * @author yuhao.wang
 */
public class RedisPublisher {
    private static final Logger logger = LoggerFactory.getLogger(RedisPublisher.class);

    private RedisTemplate<? extends Object, ? extends Object> redisTemplate;

    /**
     * 频道名称
     */
    private ChannelTopic channelTopic;

    /**
     * @param redisTemplate Redis客户端
     * @param channelTopic  频道名称
     */
    public RedisPublisher(RedisTemplate<? extends Object, ? extends Object> redisTemplate, ChannelTopic channelTopic) {
        this.channelTopic = channelTopic;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发布消息到频道（Channel）
     *
     * @param message 消息内容
     */
    public void publisher(Object message) {
        redisTemplate.convertAndSend(channelTopic.toString(), message);
        logger.info("redis消息发布者向频道【{}】发布了【{}】消息", channelTopic.toString(), message.toString());
    }
}

package com.xiaolyuh.cache.redis.utils;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import com.xiaolyuh.cache.redis.serializer.StringRedisSerializer;

/**
 * 获取默认的RedisTemplate
 *
 * @author yuhao.wang
 */
public final class RedisTemplateUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    public static RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        if (redisTemplate == null) {
            synchronized (RedisTemplateUtils.class) {
                if (redisTemplate == null) {
                    redisTemplate = new RedisTemplate<String, Object>();
                    redisTemplate.setConnectionFactory(redisConnectionFactory);

                    JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
                    redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
                    redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);

                    // 设置键（key）的序列化采用StringRedisSerializer。
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
                    redisTemplate.afterPropertiesSet();
                }
            }

        }
        return redisTemplate;
    }
}  
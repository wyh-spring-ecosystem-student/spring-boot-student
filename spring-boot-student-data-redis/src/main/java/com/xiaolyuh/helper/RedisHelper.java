package com.xiaolyuh.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * redis扩展工具
 *
 * @author yuhao.wang3
 * @since 2020/2/21 23:35
 */
public abstract class RedisHelper {
    private static Logger logger = LoggerFactory.getLogger(RedisHelper.class);

    /**
     * scan 实现
     *
     * @param redisTemplate redisTemplate
     * @param pattern       表达式，如：abc*，找出所有以abc开始的键
     */
    public static Set<String> scan(RedisTemplate<String, Object> redisTemplate, String pattern) throws NoSuchFieldException {

        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            if (connection instanceof RedisClusterConnection) {
                //集群模式
                return redisTemplate.keys(pattern);
            }

            // 单机模式
            Set<String> keysTmp = new HashSet<>();
            try (Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                    .match(pattern)
                    .count(10000).build())) {

                while (cursor.hasNext()) {
                    keysTmp.add(new String(cursor.next(), "Utf-8"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return keysTmp;
        });
    }
}

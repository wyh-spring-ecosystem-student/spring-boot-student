package com.xiaolyuh.service;

import com.xiaolyuh.lock.RedisLock;
import com.xiaolyuh.lock.RedisLock2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Service
public class PersonService {
    public static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    StringRedisTemplate redisTemplate;

    private static int j = 0;

    private static int k = 0;

    final Random random = new Random();

	public void redisLock(int i) {
        RedisLock redisLock = new RedisLock(redisTemplate, "redisLock:"+i % 10, 5*60 , 50000);
        try {
            long now = System.currentTimeMillis();
            if (redisLock.lock()) {
                logger.info("=" + (System.currentTimeMillis() - now));
                // TODO 获取到锁要执行的代码块
                logger.info("j:" + j ++);
            } else {
                logger.info("k:" + k ++);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        } finally {
            redisLock.unlock();
        }
    }

    public void redisLock2(int i) {
        RedisLock2 redisLock2 = new RedisLock2(redisTemplate, "redisLock:" + i % 10, 5 * 60, 50000);
        try {
            long now = System.currentTimeMillis();
            if (redisLock2.lock()) {
                logger.info("=" + (System.currentTimeMillis() - now));
                // TODO 获取到锁要执行的代码块
                logger.info("j:" + j++);
            } else {
                logger.info("k:" + k++);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        } finally {
            redisLock2.unlock();
        }
    }
}

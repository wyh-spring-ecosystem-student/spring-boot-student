package com.xiaolyuh.service;

import com.xiaolyuh.lock.RedisLock;
import com.xiaolyuh.lock.RedisLock2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Protocol.Command;
import redis.clients.util.SafeEncoder;

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
        RedisLock redisLock = new RedisLock(redisTemplate, "redisLock:" + i % 10, 5 * 60, 50000);
        try {
            long now = System.currentTimeMillis();
            if (redisLock.lock()) {
                logger.info("=" + (System.currentTimeMillis() - now));
                // TODO 获取到锁要执行的代码块
                logger.info("j:" + j++);
            } else {
                logger.info("k:" + k++);
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

    public String set(final String key, final String value, final String nxxx, final String expx,
                      final long seconds) {
        Assert.isTrue(!StringUtils.isEmpty(key), "key不能为空");
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                String command = Command.SET.name();
                byte[] keys = SafeEncoder.encode(key);
                byte[] values = SafeEncoder.encode(value);
                byte[] nxs = SafeEncoder.encode("NX");
                byte[] exs = SafeEncoder.encode("EX");
                byte[] secondsByte = SafeEncoder.encode(String.valueOf(seconds));
                Object result = connection.execute(command, keys, values, nxs, exs, secondsByte);
                if (result == null) {
                    return null;
                }
                return new String((byte[]) result);
            }
        });
    }
}

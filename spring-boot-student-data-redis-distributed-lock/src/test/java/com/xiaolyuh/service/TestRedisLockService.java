package com.xiaolyuh.service;

import com.xiaolyuh.redis.lock.RedisLock3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedisLockService {
    public static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    static int i = 0;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    PersonService personService;

    private String lockKey = "lock";

    @Test
    public void redisLock() {
        while (i++ < 1000) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    personService.redisLock(i);
                }
            }).start();
        }

        sleep(1000 * 60 * 2);
    }

    @Test
    public void redisLock2() {
        while (i++ < 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    personService.redisLock2(i);
                }
            }).start();
        }

        sleep(1000 * 60 * 2);
    }

    @Test
    public void testSet() {
        Object o = personService.set("lock", "11", "nx", "ex", 100L);
        System.out.println(o);
    }

    @Test
    public void testTrylock() {

        RedisLock3 redisLock3 = new RedisLock3(redisTemplate, lockKey);
        long now = System.currentTimeMillis();
        if (redisLock3.tryLock()) {
            logger.info("=" + (System.currentTimeMillis() - now));
            // TODO 获取到锁要执行的代码块
            logger.info("获取到锁");
        } else {
            logger.info("没有获取到锁");
        }

        sleep(1000 * 30);
        redisLock3.unlock();
    }

    @Test
    public void redisLock3() {
        while (i++ < 10000) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    personService.redisLock3(i);
                }
            }).start();
        }

        sleep(1000 * 60 * 2);
    }


    @Test
    public void testUUID() {
        long nowTime = System.nanoTime();
        for (int i = 0; i< 100000; i++) {
            UUID.randomUUID().toString();
        }
        System.out.println(System.nanoTime() - nowTime);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.info(e.getMessage(), e);
        }
    }
}

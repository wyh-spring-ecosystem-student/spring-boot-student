package com.xiaolyuh.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedisLockService {
    public static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    static int i = 0;

    @Autowired
    PersonService personService;

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

        sleep();
    }

    @Test
    public void redisLock2() {
        while (i++ < 1000) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    personService.redisLock2(i);
                }
            }).start();
        }

        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(999999999999999999L);
        } catch (InterruptedException e) {
            logger.info(e.getMessage(), e);
        }
    }
}

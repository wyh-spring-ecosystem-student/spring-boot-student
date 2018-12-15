package com.xiaolyuh;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yuhao.wang3
 * @since 2018/12/15 23:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CompletableFutureTest {
    Logger logger = LoggerFactory.getLogger(CompletableFutureTest.class);
    ThreadPoolTaskExecutor taskExecutor = null;

    @Before
    public void before() {
        taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(1);
        // 最大线程数
        taskExecutor.setMaxPoolSize(1);
        // 队列最大长度
        taskExecutor.setQueueCapacity(2);
        // 线程池维护线程所允许的空闲时间(单位秒)
        taskExecutor.setKeepAliveSeconds(60);
        /*
         * 线程池对拒绝任务(无限程可用)的处理策略
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
         * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
         * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务,如果执行器已关闭,则丢弃.
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        taskExecutor.initialize();
    }

    @Test
    public void testGet() throws Exception {
        for (int i = 1; i < 100; i++) {
            new Thread(() -> {
                // 第一步非常耗时，会沾满线程池
                taskExecutor.execute(() -> {
                    sleep(5000);
                });

                // 第二步不耗时的操作，但是get的时候会报TimeoutException
                CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(() -> 1, taskExecutor);
                CompletableFuture<Object> future2 = CompletableFuture.supplyAsync(() -> 2, taskExecutor);
                try {
                    System.out.println(Thread.currentThread().getName() + "::value1" + future1.get(1, TimeUnit.SECONDS));
                    System.out.println(Thread.currentThread().getName() + "::value2" + future2.get(1, TimeUnit.SECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }


        sleep(30000);
    }

    @Test
    public void testGetException() throws Exception {
        // 第二步不耗时的操作，但是get的时候会报TimeoutException
        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(() -> new RuntimeException("抛出异常"), taskExecutor);
        try {
            System.out.println(Thread.currentThread().getName() + "::value1" + future1.get(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param millis 毫秒
     * @Title: sleep
     * @Description: 线程等待时间
     * @author yuhao.wang
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.info("获取分布式锁休眠被中断：", e);
        }
    }

}

package com.xiaolyuh;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author yuhao.wang3
 * @since 2019/8/6 10:09
 */
public class ExecutorsTest {
    public static void main(String[] args) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + "   " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            //0表示首次执行任务的延迟时间，40表示每次执行任务的间隔时间，TimeUnit.MILLISECONDS执行的时间间隔数值单位
        }, 0, 1, TimeUnit.SECONDS);


        ExecutorService workStealingPool = Executors.newWorkStealingPool();

        fixedThreadPool.submit(() -> System.out.println(Thread.currentThread().getName() + "-fixedThreadPool"));
        singleThreadExecutor.submit(() -> System.out.println(Thread.currentThread().getName() + "-singleThreadExecutor"));
    }
}

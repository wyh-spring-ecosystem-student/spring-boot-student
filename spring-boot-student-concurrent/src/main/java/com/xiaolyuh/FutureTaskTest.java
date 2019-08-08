package com.xiaolyuh;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 共享锁，可以允许多个线程同时获得锁
 *
 * @author yuhao.wang3
 * @since 2019/7/12 10:00
 */
public class FutureTaskTest {

    public static void main(String[] args) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        FutureTask<Long> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步调用");
            return 10L;
        });

        fixedThreadPool.execute(futureTask);
        try {
            System.out.println(Thread.currentThread().getName() + " " + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}

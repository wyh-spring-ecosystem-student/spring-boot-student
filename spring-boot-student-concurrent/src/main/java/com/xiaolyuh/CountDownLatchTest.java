package com.xiaolyuh;

import java.util.concurrent.CountDownLatch;

/**
 * 5个初始化线程，6个扣除点，初始化完成后业务线程和住线程才能执行
 *
 * @author yuhao.wang3
 * @since 2019/6/26 15:24
 */
public class CountDownLatchTest {
    static final CountDownLatch countDownLatch = new CountDownLatch(6);

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "主线程开始......");
        new Thread(new InitJob()).start();
        new Thread(new BusinessWoerk()).start();
        new Thread(new InitJob()).start();
        new Thread(new InitWoerk()).start();
        new Thread(new InitJob()).start();
        new Thread(new InitJob()).start();

        try {
            System.out.println(Thread.currentThread().getName() + "主线程等待初始化线程初始化完成......");
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sleep(5);
        System.out.println(Thread.currentThread().getName() + "主线程结束......");
    }

    /**
     * 一个线程一个扣减点
     */
    static class InitJob implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "初始化任务开始。。。。。。");
            sleep(5);
            countDownLatch.countDown();
            sleep(5);
            System.out.println(Thread.currentThread().getName() + "初始化任务完毕后，处理业务逻辑。。。。。。");
        }
    }

    /**
     * 一个线程两个扣减点
     */
    static class InitWoerk implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "初始化工作开始第一步》》》》》");
            sleep(5);
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName() + "初始化工作开始第二步》》》》》");
            sleep(5);
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName() + "初始化工作处理业务逻辑》》》》》");
        }
    }


    /**
     * 业务线程
     */
    static class BusinessWoerk implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "初始化线程还未完成，业务线程阻塞----------");
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "初始化工作完成业务线程开始工作----------");
            System.out.println(Thread.currentThread().getName() + "初始化工作完成业务线程开始工作----------");
            System.out.println(Thread.currentThread().getName() + "初始化工作完成业务线程开始工作----------");
            System.out.println(Thread.currentThread().getName() + "初始化工作完成业务线程开始工作----------");
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

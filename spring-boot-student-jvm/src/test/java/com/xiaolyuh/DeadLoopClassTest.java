package com.xiaolyuh;

/**
 * 死循环类
 *
 * @author yuhao.wang3
 * @since 2020/01/15 17:09
 */

public class DeadLoopClassTest {


    public static void main(String[] args) {
        Runnable runnable = () -> {
            System.out.println("thread name : " + Thread.currentThread().getName() + " start");
            new DeadLoopClass();
            System.out.println("thread name : " + Thread.currentThread().getName() + " end");
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}

class DeadLoopClass {
    static {
        System.out.println("init class thread name : " + Thread.currentThread().getName());
        if (true) {
            while (true) {

            }
        }
    }
}
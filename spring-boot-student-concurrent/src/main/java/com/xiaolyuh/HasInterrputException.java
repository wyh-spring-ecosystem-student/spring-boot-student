package com.xiaolyuh;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 中断线程有异常
 *
 * @author yuhao.wang3
 */
public class HasInterrputException {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("thread:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss_SSS")));
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println(threadName + " catch interrput flag is " + Thread.currentThread().isInterrupted() +
                            " at " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss_SSS"))));
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                System.out.println(threadName);
            }
            System.out.println(threadName + " interrput flag is " + Thread.currentThread().isInterrupted());
        });
        thread.start();
        System.out.println("Main:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss_SSS")));
        Thread.sleep(800);
        System.out.println("Main begin interrupt thread:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss_SSS")));
        thread.interrupt();
    }

}
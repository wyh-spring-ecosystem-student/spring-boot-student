package com.xiaolyuh;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 守护线程
 *
 * @author yuhao.wang3
 */
public class DaemonThread {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName()
                            + " 任务执行 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss_SSS")));
                    Thread.sleep(50);
                }
                System.out.println(Thread.currentThread().getName()
                        + " 任务中断 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss_SSS")));
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("...........finally");
            }
        });
        thread.setDaemon(true);
        thread.start();
        Thread.sleep(500);
    }
}

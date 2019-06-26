package com.xiaolyuh;

/**
 * 守护线程
 *
 * @author yuhao.wang3
 */
public class JoinThread {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> System.out.println("线程1 " + Thread.currentThread().getName() + "执行完了"));

        Thread thread2 = new Thread(() -> {
            System.out.println("线程2 " + Thread.currentThread().getName() + " 开始执行");
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程2 " + Thread.currentThread().getName() + "执行完了");
        });
//        thread.setDaemon(true);
//        thread.start();
        Thread.sleep(500);
    }
}

package com.xiaolyuh;

/**
 * java 虚拟机栈和本地方法栈内存溢出测试
 * <p>
 * 创建线程过多导致内存溢出异常
 * <p>
 * VM Args: -verbose:gc -Xss20M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\dump
 *
 * @author yuhao.wang3
 * @since 2019/11/30 17:09
 */
public class StackOutOfMemoryErrorTest {
    private static int threadCount;

    public static void main(String[] args) throws Throwable {
        try {
            while (true) {
                threadCount++;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000 * 60 * 10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("threadCount=" + threadCount);
        }
    }
}

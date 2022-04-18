package com.xiaolyuh;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author yuhao.wang3
 * @since 2019/8/6 10:09
 */
public class FutureBlockTest {
    public static void main(String[] args) {
        // 为了模拟我这里只存创建一个工作线程
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        // 第一层异步任务
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + "-main-thread");
            // 第二层异步任务（嵌套任务）
            FutureTask<Long> futureTask = new FutureTask<>(() -> {
                System.out.println(Thread.currentThread().getName() + "-child-thread");
                return 10L;
            });
            fixedThreadPool.execute(futureTask);
            System.out.println("子任务提交完毕");

            // 获取子线程的返回值
            try {
                System.out.println(futureTask.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        // 提交主线
        fixedThreadPool.submit(runnable);
    }
}

package com.xiaolyuh;

import java.util.concurrent.*;

/**
 * @author yuhao.wang3
 * @since 2019/8/1 16:38
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutorExt(1, 2, 1L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(2));

        executor.execute(() -> System.out.println("当前任务线程被执行"));
//        executor.execute(() -> new Exception("dddddd"));
//        executor.execute(() -> new Exception("dddddd"));
//        executor.execute(() -> new Exception("dddddd"));

        // 获取活动的线程数。
        System.out.println(executor.getActiveCount());
        // 线程池的线程数量。
        System.out.println(executor.getPoolSize());
        // 线程池在运行过程中已完成的任务数量，小于或等于taskCount。
        System.out.println(executor.getCompletedTaskCount());
        // 线程池需要执行的任务数量。
        System.out.println(executor.getTaskCount());
        // 线程池里曾经创建过的最大线程数量。
        System.out.println(executor.getLargestPoolSize());
    }

    static class ThreadPoolExecutorExt extends ThreadPoolExecutor {

        public ThreadPoolExecutorExt(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public ThreadPoolExecutorExt(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        public ThreadPoolExecutorExt(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }

        public ThreadPoolExecutorExt(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            throw new RuntimeException("deeded");
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
        }
    }
}

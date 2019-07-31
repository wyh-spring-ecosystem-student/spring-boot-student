package com.xiaolyuh;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 计算1+2+3+...+n的值
 * 使用同步执行的方式
 *
 * @author yuhao.wang3
 * @since 2019/6/25 17:07
 */
public class ForkJoinCountTask extends RecursiveTask<Long> {

    /**
     * 阀值
     */
    private int threshold = 10;

    /**
     * 任务的开始值
     */
    private long start;

    /**
     * 任务的结束值
     */
    private long end;

    public ForkJoinCountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= threshold) {
            long count = 0;
            for (int i = 0; i <= end - start; i++) {
                count = count + start + i;
            }
            System.out.println(Thread.currentThread().getName() + " 小计结果: " + count);
            return count;
        } else {
            // 如果任务大于阈值，就分裂成三个子任务计算
            long slip = (end - start) / 3;
            ForkJoinCountTask oneTask = new ForkJoinCountTask(start, start + slip);
            ForkJoinCountTask twoTask = new ForkJoinCountTask(start + slip + 1, start + slip * 2);
            ForkJoinCountTask threeTask = new ForkJoinCountTask(start + slip * 2 + 1, end);
            // 提交子任务到框架去执行
            invokeAll(oneTask, twoTask, threeTask);
            // 等待子任务执行完，得到其结果，并合并子任务
            return oneTask.join() + twoTask.join() + threeTask.join();
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        // 生成一个计算任务，负责计算1+2+3+n
        ForkJoinCountTask countTask = new ForkJoinCountTask(1, 1000000);
        // 执行一个任务（同步执行，任务会阻塞在这里直到任务执行完成）
        pool.invoke(countTask);
        // 异常检查
        if (countTask.isCompletedAbnormally()) {
            Throwable throwable = countTask.getException();
            if (Objects.nonNull(throwable)) {
                System.out.println(throwable.getMessage());
            }
        }
        // join方法是一个阻塞方法，会等待任务执行完成
        System.out.println("计算为：" + countTask.join() + ", 耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }
}

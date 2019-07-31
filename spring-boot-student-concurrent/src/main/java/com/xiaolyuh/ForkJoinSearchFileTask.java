package com.xiaolyuh;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 搜索指定目录下的指定文件
 * 使用异步执行的方式
 *
 * @author yuhao.wang3
 * @since 2019/6/25 17:07
 */
public class ForkJoinSearchFileTask extends RecursiveAction {

    /**
     * 指定目录
     */
    private File file;

    /**
     * 文件后缀
     */
    private String suffix;

    public ForkJoinSearchFileTask(File file, String suffix) {
        this.file = file;
        this.suffix = suffix;
    }

    @Override
    protected void compute() {
        if (Objects.isNull(file)) {
            return;
        }

        File[] files = file.listFiles();
        List<ForkJoinSearchFileTask> fileTasks = new ArrayList<>();
        if (Objects.nonNull(files)) {
            for (File f : files) {
                // 拆分任务
                if (f.isDirectory()) {
                    fileTasks.add(new ForkJoinSearchFileTask(f, suffix));
                } else {
                    if (f.getAbsolutePath().endsWith(suffix)) {
                        System.out.println(Thread.currentThread().getName() + "  文件： " + f.getAbsolutePath());
                    }
                }
            }
            // 提交并执行任务
            invokeAll(fileTasks);
            for (ForkJoinSearchFileTask fileTask : fileTasks) {
                // 等待任务执行完成
                fileTask.join();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        File file = new File("d:/");
        ForkJoinPool pool = new ForkJoinPool();
        // 生成一个计算任务，负责查找指定木目录
        ForkJoinSearchFileTask searchFileTask = new ForkJoinSearchFileTask(file, ".txt");
        // 异步执行一个任务
        pool.execute(searchFileTask);

        Thread.sleep(10);

        // 做另外的事情
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            count += i;
        }
        System.out.println("计算任务：" + count);

        // join方法是一个阻塞方法，会等待任务执行完成
        searchFileTask.join();
    }
}

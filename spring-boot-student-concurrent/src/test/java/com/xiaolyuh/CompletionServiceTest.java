package com.xiaolyuh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.*;

/**
 * CompletionService  示例
 *
 * @RunWith(SpringRunner.class)
 * @SpringBootTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CompletionServiceTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        Random random = new Random();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor, new LinkedBlockingQueue<>(10));
        for (int i = 0; i < 8; i++) {
            completionService.submit(() -> {
                int time = random.nextInt(1000);
                sleep(time);
                System.out.println(Thread.currentThread().getName() + " 执行异步任务执行耗时： " + time);
                return time;
            });
        }

        while (true) {
            System.out.println(Thread.currentThread().getName() + " 主线程获取到任务结果 " + completionService.take().get());
        }
    }


    public static void sleep(int probe) {
        try {
            Thread.sleep(probe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


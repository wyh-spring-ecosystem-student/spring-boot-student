package com.xiaolyuh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootStudentConcurrentApplicationTests {

    @Test
    public void runnableTest() {
        // JDK 1.7
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "::Runnable异步任务1");
            }
        }).start();

        // JDK 1.8
        new Thread(() -> System.out.println(Thread.currentThread().getName() + "::Runnable异步任务2")).start();
    }

    @Test
    public void callableTest() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        // JDK 1.7
        FutureTask<String> futureTask1 = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Thread.currentThread().getName() + "::Callable异步任务1";
            }
        });

        // JDK 1.8
        FutureTask<String> futureTask2 = new FutureTask<>(() -> Thread.currentThread().getName() + "::Callable异步任务2");

        service.submit(futureTask1);
        service.submit(futureTask2);
        try {
            System.out.println(futureTask1.get(5, TimeUnit.SECONDS));
            System.out.println(futureTask2.get(5, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCopyOnWrite() {
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add(5);
        copyOnWriteArrayList.add(3);
        copyOnWriteArrayList.add(4);
        copyOnWriteArrayList.add(2);
        copyOnWriteArrayList.add(3);

        System.out.println(copyOnWriteArrayList);


        CopyOnWriteArraySet<Integer> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
        copyOnWriteArraySet.add(5);
        copyOnWriteArraySet.add(3);
        copyOnWriteArraySet.add(4);
        copyOnWriteArraySet.add(2);
        copyOnWriteArraySet.add(3);

        System.out.println(copyOnWriteArraySet);
    }

    @Test
    public void singleton() {
        System.out.println(EnumSingleton.getInstance());
    }
}

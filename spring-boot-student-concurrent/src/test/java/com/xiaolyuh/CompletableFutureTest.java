package com.xiaolyuh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * CompletableFuture 示例
 *
 * @RunWith(SpringRunner.class)
 * @SpringBootTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CompletableFutureTest {

    /**
     * CompletableFuture 提供了四个静态方法来创建一个异步操作。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testRunAsync() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture.runAsync(() -> System.out.println(Thread.currentThread().getName() + " 执行异步任务 runAsync"), executor);
        String result = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync");
            return "有结果";
        }, executor).get();
        System.out.println(result);
    }

    /**
     * 当CompletableFuture的计算结果完成，或者抛出异常的时候，可以执行特定的Action。主要是下面的方法：
     */
    @Test
    public void testWhenComplete() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync");
            return "有结果";
        }, executor).whenComplete((s, throwable) -> {
            System.out.println(Thread.currentThread().getName() + " 执行了 whenComplete");
            if (!StringUtils.isEmpty(s)) {
                System.out.println(Thread.currentThread().getName() + " 真的有结果诶！ 结果是:" + s);
            }
        }).whenCompleteAsync((s, throwable) -> {
            System.out.println(Thread.currentThread().getName() + " 执行了 whenCompleteAsync");
            System.out.println(Thread.currentThread().getName() + " 打印结果值 :" + s);
        }, executor).exceptionally(throwable -> {
            System.out.println(Thread.currentThread().getName() + " 执行了 exceptionally");
            System.out.println(Thread.currentThread().getName() + " 异常了 :" + throwable.getMessage());
            return "异常了";
        });
    }

    /**
     * 当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testThenApply() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Integer integer = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync");
            return "有结果";
        }, executor).thenApplyAsync(s -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 thenApply");
            if (!StringUtils.isEmpty(s)) {
                return s.length();
            }
            return 0;
        }, executor).get();
        System.out.println(Thread.currentThread().getName() + " " + integer);
    }

    /**
     * handle 是执行任务完成时对结果的处理。
     * handle 方法和thenApply 方法处理方式基本一样。不同的是 handle 是在任务完成后再执行，还可以处理异常的任务。thenApply 只可以执行正常的任务，任务出现异常则不执行 thenApply 方法。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testHandle() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Integer integer = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync");
            return 10 / 0;
        }, executor).handleAsync((s, throwable) -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 handleAsync");

            if (Objects.nonNull(throwable)) {
                System.out.println(Thread.currentThread().getName() + " 异常信息 " + throwable.getMessage());
                return 1;
            }

            return s;
        }, executor).get();
        System.out.println(Thread.currentThread().getName() + " " + integer);
    }

    /**
     * 接收任务的处理结果，并消费处理，无返回结果。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testThenAccept() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync");
            return 10;
        }, executor).thenAcceptAsync(s -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 thenAccept");
            System.out.println(Thread.currentThread().getName() + " " + s);
        }, executor).get();
    }

    /**
     * 跟 thenAccept方法不一样的是，thenRun不关心任务的处理结果。只要上面的任务执行完成，就开始执行 thenRun里面的任务。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testThenRun() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync");
            return 10;
        }, executor).thenRunAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 thenRun");
        }, executor).get();
    }

    /**
     * thenCombine 会把 两个CompletionStage的任务都执行完成后，把两个任务的结果一块交给thenCombine来处理。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testThenCombine() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Integer result = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync1");
            return 10;
        }, executor).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync2");
            return 15;
        }, executor), (result1, result2) -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 thenCombine");
            return result1 + result2;
        }).get();
        System.out.println(Thread.currentThread().getName() + "  " + result);

        // 分解一下好看些
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync1");
            return 10;
        }, executor);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync2");
            return 15;
        }, executor);

        result = future1.thenCombine(future2, (result1, result2) -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 thenCombine");
            return result1 + result2;
        }).get();
        System.out.println(Thread.currentThread().getName() + "  " + result);
    }

    /**
     * 当两个CompletionStage都执行完成后，把结果一块交给thenAcceptBoth来进行消耗。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testThenAcceptBoth() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 分解一下好看些
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync1");
            return 10;
        }, executor);

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync2");
            return 15;
        }, executor);

        future1.thenAcceptBoth(future2, (result1, result2) -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 thenAcceptBoth");
            System.out.println(Thread.currentThread().getName() + " " + (result1 + result2));
        }).get();
    }

    /**
     * 两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的转化操作。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testApplyToEither() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        // 分解一下好看些
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync1   " + probe);
            CompletableFutureTest.sleep(probe);
            return probe;
        }, executor);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync2   " + probe);
            CompletableFutureTest.sleep(probe);
            return probe;
        }, executor);

        String result = future1.applyToEither(future2, integer -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 applyToEither");
            return integer + "";
        }).get();
        System.out.println(Thread.currentThread().getName() + " 执行异步任务 " + result);
    }

    /**
     * 两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的消耗操作。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testAcceptEither() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        // 分解一下好看些
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync1   " + probe);
            CompletableFutureTest.sleep(probe);
            return probe;
        }, executor);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync2   " + probe);
            CompletableFutureTest.sleep(probe);
            return probe;
        }, executor);

        future1.acceptEither(future2, integer -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 acceptEither  " + integer);
        }).get();
    }

    /**
     * 两个CompletionStage，任何一个完成了都会执行下一步的操作（Runnable）
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testRunAfterEither() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        // 分解一下好看些
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync1   " + probe);
            CompletableFutureTest.sleep(probe);
            return probe;
        }, executor);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync2   " + probe);
            CompletableFutureTest.sleep(probe);
            return probe;
        }, executor);

        future1.runAfterEither(future2, () -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 runAfterEither  ");
        }).get();

        System.out.println(Thread.currentThread().getName() + " 任务结束 耗时：" + (System.currentTimeMillis() - start));
    }

    /**
     * 两个CompletionStage，都完成了计算才会执行下一步的操作（Runnable）
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testRunAfterBoth() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        // 分解一下好看些
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync1   " + probe);
            CompletableFutureTest.sleep(probe);
            return probe;
        }, executor);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync2   " + probe);
            CompletableFutureTest.sleep(probe);
            return probe;
        }, executor);

        future1.runAfterBoth(future2, () -> {
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 runAfterBoth   ");
        }).get();

        System.out.println(Thread.currentThread().getName() + " 任务结束 耗时：" + (System.currentTimeMillis() - start));
    }

    /**
     * thenCompose方法允许你对两个CompletionStage 进行流水线操作，第一个操作完成时，将其结果作为参数传递给第二个操作。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testThenCompose() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        // 分解一下好看些
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync1   " + probe);
            CompletableFutureTest.sleep(probe);
            return probe;
        }, executor);


        String result = future1.thenCompose(integer -> CompletableFuture.supplyAsync(() -> {
            int probe = ThreadLocalRandom.current().nextInt(1000);
            System.out.println(Thread.currentThread().getName() + " 执行异步任务 supplyAsync1   " + probe);
            CompletableFutureTest.sleep(probe);
            return integer + "  " + probe;
        }, executor)).get();

        System.out.println(Thread.currentThread().getName() + "  " + result);
        System.out.println(Thread.currentThread().getName() + " 任务结束 耗时：" + (System.currentTimeMillis() - start));
    }


    public static void sleep(int probe) {
        try {
            Thread.sleep(probe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


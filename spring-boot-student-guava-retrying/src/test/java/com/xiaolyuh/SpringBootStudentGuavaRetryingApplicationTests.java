package com.xiaolyuh;

import com.github.rholder.retry.*;
import com.xiaolyuh.service.RetryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootStudentGuavaRetryingApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(SpringBootStudentGuavaRetryingApplicationTests.class);

    @Autowired
    private RetryService retryService;

    @Test
    public void testRetrying() {
//        Callable<Object> task = new Callable<Object>() {
//            @Override
//            public Object call() throws Exception {
//                System.out.println("runtime was called.");
//                return null;
//            }
//        };

        Callable<Object> task = () -> getTask(1);

        Retryer<Object> retryer = RetryerBuilder.newBuilder()
                .retryIfExceptionOfType(Exception.class)// 抛出Exception异常时重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 重试3次后停止
                .withWaitStrategy(WaitStrategies.fixedWait(300, TimeUnit.MILLISECONDS))// 等待300毫秒
                .build();

        try {
            retryer.call(task);
        } catch (ExecutionException e) {
            logger.error(e.getMessage(), e);
        } catch (RetryException e) {
            logger.error(e.getMessage(), e);
        }

    }

    private Object getTask(int a) {
        System.out.println("runtime was called.");
        if (a == 1) {
            throw new RuntimeException("执行任务异常");
        }
        return null;
    }

    @Test
    public void testRetryingAnn() {

        retryService.retry();

    }

}

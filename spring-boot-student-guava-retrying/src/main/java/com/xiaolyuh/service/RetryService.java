package com.xiaolyuh.service;

import com.xiaolyuh.annotation.Retryable;
import com.xiaolyuh.retrylistener.MyRetryListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RetryService {
    @Retryable(exception = Throwable.class, attemptNumber = 4, sleepTime = 300, timeUnit = TimeUnit.MILLISECONDS, retryListener = MyRetryListener.class)
    public Object retry() {
        System.out.println("测试方法重试");

        return null;
    }
}

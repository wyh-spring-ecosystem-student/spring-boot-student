package com.xiaolyuh.annotation;

import com.github.rholder.retry.RetryListener;
import com.xiaolyuh.retrylistener.DefaultRetryListener;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Retryable {
    /**
     * 异常信息
     */
    Class<? extends Throwable> exception() default Exception.class;

    /**
     * 重试次数
     */
    int attemptNumber() default 3;

    /**
     * 等待时间
     */
    long sleepTime() default 3;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 监听器
     */
    Class<? extends RetryListener> retryListener() default DefaultRetryListener.class;
}

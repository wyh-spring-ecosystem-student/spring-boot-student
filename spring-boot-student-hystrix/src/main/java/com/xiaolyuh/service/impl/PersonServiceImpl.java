package com.xiaolyuh.service.impl;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiaolyuh.entity.Person;
import com.xiaolyuh.entity.Result;
import com.xiaolyuh.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    RedisTemplate redisTemplate;

    @HystrixCommand(groupKey = "hystrixSemaphoreTestGroupKey", commandKey = "hystrixSemaphoreTestCommandKey",
            fallbackMethod = "fallbackMethodSemaphore",
            commandProperties = {
                    //指定多久超时，单位毫秒。超时进fallback
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
                    //判断熔断的最少请求数，默认是10；只有在一个统计窗口内处理的请求数量达到这个阈值，才会进行熔断与否的判断
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    //判断熔断的阈值，默认值50，表示在一个统计窗口内有50%的请求处理失败，会触发熔断
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "80"),
                    //熔断多少毫秒后开始尝试请求 默认5000ms
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
                    // 最大并发请求数，默认10，该参数当使用ExecutionIsolationStrategy.SEMAPHORE策略时才有效。
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "10"),
                    // 如果并发数达到该设置值，请求会被拒绝和抛出异常并且fallback不会被调用。默认10
                    @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value = "100")
            }
    )
    @Override
    public Result semaphore(String arg) {
        redisTemplate.opsForValue().get("semaphore");
        Person person = new Person();
        person.setAge(18);
        person.setId(2L);
        person.setName("名称semaphore");
        person.setAddress("地址semaphore");
        logger.info(JSON.toJSONString(person));
        return Result.success(person);
    }

    // com.netflix.hystrix.HystrixCommandProperties  查看所有配置
    @HystrixCommand(groupKey = "hystrixThreadTestGroupKey", commandKey = "hystrixThreadTestCommandKey",
            fallbackMethod = "fallbackMethodThread",
            commandProperties = {
                    //指定多久超时，单位毫秒。超时进fallback
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
                    //判断熔断的最少请求数，默认是10；只有在一个统计窗口内处理的请求数量达到这个阈值，才会进行熔断与否的判断
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    //判断熔断的阈值，默认值50，表示在一个统计窗口内有50%的请求处理失败，会触发熔断
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "90"),
                    //熔断多少毫秒后开始尝试请求 默认5000ms
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000"),
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    // 如果并发数达到该设置值，请求会被拒绝和抛出异常并且fallback不会被调用。默认10
                    @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value = "100"),
                    // 统计错误率的一个滑动窗口的时间，默认10000ms， 10秒
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
                    // 生成统计数据时滑动窗口拆分的桶数，metrics.rollingStats.timeInMilliseconds % metrics .rollingStats.numBuckets 必须为0
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            },
            // HystrixThreadPoolProperties
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueueSize", value = "1000"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "800"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1")
            }
    )
    @Override
    public Result thread(String arg) {
        redisTemplate.opsForValue().get("thread");
        Person person = new Person();
        person.setAge(18);
        person.setId(2L);
        person.setName("名称thread");
        person.setAddress("地址thread");
        logger.info(JSON.toJSONString(person));
        return Result.success(person);
    }

    public Result fallbackMethodSemaphore(String arg, Throwable throwable) {
        // 获取断路器状态
        HystrixCircuitBreaker circuitBreaker = HystrixCircuitBreaker.Factory.getInstance(
                HystrixCommandKey.Factory.asKey("hystrixThreadTestCommandKey"));
        logger.info("熔断降级 Circuit is open {}", !circuitBreaker.allowRequest());
        return Result.error(String.format("熔断降级 Circuit is open %s", !circuitBreaker.allowRequest()));
    }

    public Result fallbackMethodThread(String arg) {
        // 获取断路器状态
        HystrixCircuitBreaker circuitBreaker = HystrixCircuitBreaker.Factory.getInstance(
                HystrixCommandKey.Factory.asKey("hystrixThreadTestCommandKey"));
        logger.info("熔断降级 Circuit is open {}", !circuitBreaker.allowRequest());
        return Result.error(String.format("熔断降级 Circuit is open %s", !circuitBreaker.allowRequest()));
    }
}

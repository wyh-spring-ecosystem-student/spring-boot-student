package com.xiaolyuh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class ChildThreadService {
    Logger logger = LoggerFactory.getLogger(ChildThreadService.class);

    @Async
    public Future<Long> asyncMethod() throws Exception {
        // 子任务执行
        Thread.sleep(1000);
        logger.info("-------------------- {}", 10L);
        // 返回异步结果
        return new AsyncResult<>(10L);
    }
}
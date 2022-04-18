package com.xiaolyuh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class MainThreadService {
    Logger logger = LoggerFactory.getLogger(MainThreadService.class);
    @Autowired
    private ChildThreadService childThreadService;

    @Async
    public void asyncMethod() throws Exception {
        // 主任务开始
        // TODO
        // 开启子任务
        Future<Long> longFuture = childThreadService.asyncMethod();
        // 子任务阻塞子任务
        Long aLong = longFuture.get();
        // TODO
        logger.info("======== {}", aLong);
    }
}

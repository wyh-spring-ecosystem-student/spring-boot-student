package com.xiaolyuh.service;

import com.xiaolyuh.utils.ThreadTaskUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.FutureTask;

@Service
public class LogService {
    Logger logger = LoggerFactory.getLogger(LogService.class);

    public void log() {
        logger.debug("==============================================");
        ThreadTaskUtils.run(() -> run());
        FutureTask<String> futureTask = new FutureTask<String>(() -> call());
        ThreadTaskUtils.run(futureTask);
        try {
            logger.debug("===================: {}", futureTask.get());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.debug("==============================================");
    }

    private String call() {
        logger.debug("11111111111");
        return "3333";
    }

    public void run() {
        logger.debug("222222222222222");
    }
}

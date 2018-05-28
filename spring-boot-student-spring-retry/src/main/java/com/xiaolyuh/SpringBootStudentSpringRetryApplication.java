package com.xiaolyuh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry// 启动Spring retry
public class SpringBootStudentSpringRetryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStudentSpringRetryApplication.class, args);
    }

}

package com.xiaolyuh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringBootStudentConcurrentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStudentConcurrentApplication.class, args);
    }
}

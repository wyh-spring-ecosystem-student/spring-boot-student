package com.xiaolyuh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootStudentSentinelApplication {

    public static void main(String[] args) {
        // -Dcsp.sentinel.dashboard.server=127.0.0.1:8900 -Dproject.name=sentinel-demo
        SpringApplication.run(SpringBootStudentSentinelApplication.class, args);
    }
}

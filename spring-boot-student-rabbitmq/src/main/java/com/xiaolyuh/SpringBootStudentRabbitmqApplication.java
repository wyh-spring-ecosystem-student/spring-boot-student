package com.xiaolyuh;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class SpringBootStudentRabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStudentRabbitmqApplication.class, args);
    }
}

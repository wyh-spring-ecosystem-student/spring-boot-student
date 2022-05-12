package com.xiaolyuh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * -javaagent:/XX/skywalking-agent/skywalking-agent.jar -DSW_AGENT_NAME=demoskywalking
 */
@SpringBootApplication
public class SpringBootStudentWebSocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStudentWebSocketApplication.class, args);
    }
}

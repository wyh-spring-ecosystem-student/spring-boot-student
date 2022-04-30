package com.xiaolyuh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * -javaagent:/XX/skywalking-agent/skywalking-agent.jar -DSW_AGENT_NAME=demoskywalking
 */
@SpringBootApplication
//@EnableLayeringCache
public class SpringBootStudentSkywalkingCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStudentSkywalkingCacheApplication.class, args);
    }
}

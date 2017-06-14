package com.xiaolyuh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootStudentLogApplication {
	private static final Logger logger = LoggerFactory.getLogger(SpringBootStudentLogApplication.class);
	public static void main(String[] args) {
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		logger.debug("==============================================");
		SpringApplication.run(SpringBootStudentLogApplication.class, args);
	}
}

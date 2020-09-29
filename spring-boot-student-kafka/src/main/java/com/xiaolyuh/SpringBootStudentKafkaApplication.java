package com.xiaolyuh;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootStudentKafkaApplication {

    // 标准的JAVA应用main方法，主要作用作为项目启动的入口
	public static void main(String[] args) {
//		SpringApplication.run(SpringBootStudentBannerApplication.class, args);
		SpringApplication application = new SpringApplication(SpringBootStudentKafkaApplication.class);
		/*
		 * Mode.OFF:关闭; 
		 * Mode.CONSOLE:控制台输出，默认方式;
		 * Mode.LOG:日志输出方式;
		 */
		application.setBannerMode(Mode.CONSOLE);
		application.run(args);
	}
}

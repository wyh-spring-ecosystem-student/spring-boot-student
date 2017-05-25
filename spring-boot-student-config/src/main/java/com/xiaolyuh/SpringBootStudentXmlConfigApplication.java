package com.xiaolyuh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:config/some-context.xml"})
public class SpringBootStudentXmlConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStudentXmlConfigApplication.class, args);
	}
}

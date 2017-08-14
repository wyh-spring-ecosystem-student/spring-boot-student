package com.xiaolyuh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching// 开启缓存，需要显示的指定
public class SpringBootStudentCacheEhCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStudentCacheEhCacheApplication.class, args);
	}
}

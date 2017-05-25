package com.xiaolyuh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "author")// prefix指定properties的前缀，
public class AuthorSettings {
	private String name;
	
	private long age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "AuthorSettings [name=" + name + ", age=" + age + "]";
	}
	
}

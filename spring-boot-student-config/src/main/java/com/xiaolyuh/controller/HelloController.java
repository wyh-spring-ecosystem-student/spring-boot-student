package com.xiaolyuh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaolyuh.config.PropertyConfig;
import com.xiaolyuh.entity.Person;

@RestController
public class HelloController {
	
	@Autowired
	private Person person;

	@Autowired
	private PropertyConfig propertyConfig;
	
	@GetMapping("xml")
	public Object xml() {
		
		return person;
	}

	@GetMapping("property")
	public Object property() {
		
		return propertyConfig.toString();
	}
}

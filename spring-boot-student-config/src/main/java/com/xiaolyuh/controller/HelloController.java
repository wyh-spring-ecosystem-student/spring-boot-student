package com.xiaolyuh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaolyuh.entity.Person;

@RestController
public class HelloController {
	
	@Autowired
	private Person person;
	
	@GetMapping("index")
	public Object index() {
		
		return person;
	}
}

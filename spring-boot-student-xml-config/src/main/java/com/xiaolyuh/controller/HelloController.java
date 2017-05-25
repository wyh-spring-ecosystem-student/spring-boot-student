package com.xiaolyuh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@GetMapping("index")
	public Object index() {
		
		return "你好";
	}
}

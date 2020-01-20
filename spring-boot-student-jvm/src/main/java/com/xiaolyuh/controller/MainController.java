package com.xiaolyuh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhao.wang
 */
@RestController
public class MainController {

    @GetMapping("/")
    public String helloWorld() {

        return "Hello World";
    }
}
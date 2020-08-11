package com.xiaolyuh.controller;

import com.xiaolyuh.entity.Result;
import com.xiaolyuh.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SentinelController {
    @Autowired
    PersonService personService;

    @RequestMapping("/semaphore")
    public Result semaphore() {

        return personService.semaphore("");
    }

    @RequestMapping("/thread")
    public Result thread() {

        return personService.thread("");
    }

}
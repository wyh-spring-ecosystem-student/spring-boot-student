package com.xiaolyuh.controller;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.entity.Result;
import com.xiaolyuh.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HystrixController {
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
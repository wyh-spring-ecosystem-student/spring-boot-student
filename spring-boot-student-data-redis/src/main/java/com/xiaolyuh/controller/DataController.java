package com.xiaolyuh.controller;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/set") //1
    public void set(){
        Person person = new Person("1","wyf", 32);
        personRepository.save(person);
        personRepository.stringRedisTemplateDemo();
    }

    @RequestMapping("/getStr") //2
    public String getStr(){
        return personRepository.getString();
    }

    @RequestMapping("/getPerson") //3
    public Person getPerson(){
        return personRepository.getPerson();
    }

}
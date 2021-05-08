package com.xiaolyuh.controller;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(value = "set", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void set() {
        Person person = new Person("1", "wyf", 32);
        personRepository.save(person);
        personRepository.stringRedisTemplateDemo();
    }

    @RequestMapping(value = "get-str", produces = MediaType.APPLICATION_JSON_UTF8_VALUE) //2
    public String getStr() {
        return personRepository.getString();
    }

    @RequestMapping(value = "get-person", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Person getPerson() {
        return personRepository.getPerson();
    }

}
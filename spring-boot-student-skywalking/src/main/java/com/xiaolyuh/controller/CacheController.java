package com.xiaolyuh.controller;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {

    @Autowired
    PersonService personService;

    @RequestMapping("/put")
    public long put(@RequestBody Person person) {
        Person p = personService.save(person);
        return p.getId();
    }

    @RequestMapping("/able")
    public Person cacheable(@RequestBody Person person) throws Exception {

        return personService.findOne(person);
    }

    @RequestMapping("/able1")
    public Person cacheable1(@RequestBody Person person) throws Exception {

        return personService.findOne1(person);
    }

    @RequestMapping("/evit")
    public String evit(Long id) {

        personService.remove(id);
        return "ok";
    }

}
package com.xiaolyuh.controller;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    public Person cacheable(Person person) {
        String a = "a";
        String[] b = {"1", "2"};
        List<Long> c = new ArrayList<>();
        c.add(3L);
        c.add(4L);
        c.add(5L);
        return personService.findOne(person, a, b, c);
    }

    @RequestMapping("/able1")
    public Person cacheable1(Person person) {

        return personService.findOne1();
    }

    @RequestMapping("/able2")
    public Person cacheable2(Person person) {

        return personService.findOne2(person);
    }

    @RequestMapping("/evit")
    public String evit(Long id) {

        personService.remove(id);
        return "ok";
    }

}
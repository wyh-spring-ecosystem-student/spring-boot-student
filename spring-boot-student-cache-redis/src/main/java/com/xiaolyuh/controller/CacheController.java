package com.xiaolyuh.controller;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CacheController {

    @Autowired
    PersonService personService;

    @Autowired
    CacheManager cacheManager;

    @RequestMapping("/put")
    public long put(@RequestBody Person person) {
        Person p = personService.save(person);
        return p.getId();
    }

    @RequestMapping("/able")
    public Person cacheable(Person person) {

        return personService.findOne(person, "q", 1L, true, 'a', new BigDecimal(1));
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
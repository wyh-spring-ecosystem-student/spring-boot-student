package com.xiaolyuh.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xiaolyuh.entity.Person;
import com.xiaolyuh.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@RestController
public class CacheController {

    @Autowired
    PersonService personService;

    Cache<String, Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

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

    @RequestMapping("/testGet")
    public Object cacheable1(Person person) {
        String key = "name";

        // Lookup an entry, or null if not found
        Object graph = cache.getIfPresent(key);
        // Lookup and compute an entry if absent, or null if not computable
        graph = cache.get(key, k -> createExpensiveGraph(k));
        // Insert or update an entry
        cache.put(key, graph);
        // Remove an entry
        cache.invalidate(key);

        ConcurrentMap<String, Object> map = cache.asMap();
        System.out.println(map.toString());
        return graph;
    }

    private Object createExpensiveGraph(String key) {
        System.out.println("调用了该方法获取缓存key的值");
        if (key.equals("name")) {
            throw new RuntimeException("调用了该方法获取缓存key的值的时候出现异常");
        }
        return null;
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
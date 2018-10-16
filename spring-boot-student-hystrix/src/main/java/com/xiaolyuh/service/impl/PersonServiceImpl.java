package com.xiaolyuh.service.impl;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Person save(Person person) {
        Person p = new Person();
        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    public void remove(Long id) {
        System.out.println("删除了id、key为" + id + "的数据缓存");
        //这里不做实际删除操作
    }

    @Override
    public Person findOne(Person person, String a, String[] b, List<Long> c) {
        Person p = new Person();
        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");
        System.out.println(redisTemplate);
        return p;
    }

    @Override
    public Person findOne1() {
        Person p = new Person();
        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    public Person findOne2(Person person) {
        Person p = new Person();
        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }
}

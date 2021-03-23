package com.xiaolyuh.service.impl;

import com.github.xiaolyuh.annotation.*;
import com.github.xiaolyuh.support.ExpireMode;
import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import com.xiaolyuh.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PersonServiceImpl implements PersonService {
    Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    PersonRepository personRepository;

    @Override
    @CachePut(value = "people", key = "#person.id", depict = "用户信息缓存")
    public Person save(Person person) {
        Person p = personRepository.save(person);
        logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    @CacheEvict(value = "people", key = "#id")//2
    public void remove(Long id) {
        logger.info("删除了id、key为" + id + "的数据缓存");
        //这里不做实际删除操作
    }

    @Override
    @CacheEvict(value = "people", allEntries = true)//2
    public void removeAll() {
        logger.info("删除了所有缓存的数据缓存");
        //这里不做实际删除操作
    }

    @Override
    @Cacheable(value = "'people' + ':' + #person.id", key = "#person.id", depict = "用户信息缓存", enableFirstCache = false,
            firstCache = @FirstCache(expireTime = 4, timeUnit = TimeUnit.MINUTES),
            secondaryCache = @SecondaryCache(expireTime = 15, preloadTime = 8, forceRefresh = true, timeUnit = TimeUnit.HOURS))
    public Person findOne(Person person) {
        Person p = personRepository.findOne(Example.of(person));
        logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    @Cacheable(value = "people1", key = "#person.id", depict = "用户信息缓存1", enableFirstCache = false,
            firstCache = @FirstCache(expireTime = 4,timeUnit = TimeUnit.MINUTES, expireMode = ExpireMode.ACCESS),
            secondaryCache = @SecondaryCache(expireTime = 15, preloadTime = 8, forceRefresh = true))
    public Person findOne1(Person person) {
        Person p = personRepository.findOne(Example.of(person));
        logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }
}

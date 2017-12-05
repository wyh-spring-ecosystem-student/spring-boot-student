package com.xiaolyuh.service.impl;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import com.xiaolyuh.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yuhao.wang
 */
@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonRepository personRepository;

    @Override
    @CachePut(value = "people", key = "#person.id")
    public Person save(Person person) {
        Person p = personRepository.save(person);
        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    @CacheEvict(value = "people")//2
    public void remove(Long id) {
        System.out.println("删除了id、key为" + id + "的数据缓存");
        //这里不做实际删除操作
    }

    /**
     * Cacheable
     * value：缓存key的前缀。
     * key：缓存key的后缀。
     * sync：设置如果缓存过期是不是只放一个请求去请求数据库，其他请求阻塞，默认是false。
     */
    @Override
    @Cacheable(value = "people", key = "#person.id", sync = true)
    public Person findOne(Person person, String a, String[] b, List<Long> c) {
        Person p = personRepository.findOne(person.getId());
        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    @Cacheable(value = "people1")//3
    public Person findOne1() {
        Person p = personRepository.findOne(2L);
        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    @Cacheable(value = "people2")//3
    public Person findOne2(Person person) {
        Person p = personRepository.findOne(person.getId());
        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }
}

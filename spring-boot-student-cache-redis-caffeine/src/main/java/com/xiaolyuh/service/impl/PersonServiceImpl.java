package com.xiaolyuh.service.impl;

import com.xiaolyuh.cache.LayCacheable;
import com.xiaolyuh.cache.setting.FirstCacheSetting;
import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import com.xiaolyuh.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    PersonRepository personRepository;

    @Override
    @CachePut(value = "people1", key = "#person.id")
    public Person save(Person person) {
        Person p = personRepository.save(person);
        logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    @CacheEvict(value = "people1", key = "#id")//2
    public void remove(Long id) {
        logger.info("删除了id、key为" + id + "的数据缓存");
        //这里不做实际删除操作
    }

    /**
     * LayCacheable
     * value：缓存key的前缀。
     * key：缓存key的后缀。
     * sync：设置如果缓存过期是不是只放一个请求去请求数据库，其他请求阻塞，默认是false。
     */
    @Override
    @Cacheable(value = "people")
    public Person findOne() {
        Person p = personRepository.findOne(2L);
        logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    @Cacheable(value = "people1", key = "#person.id", sync = true)//3
    @LayCacheable(fcs = FirstCacheSetting.class, ccc = @Cacheable(value = "people1", key = "#person.id", sync = true))
    public Person findOne1(Person person, String a, String[] b, List<Long> c) {
        Person p = personRepository.findOne(person.getId());
        if (p != null) {
            logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        }
        return p;
    }

    @Override
    @Cacheable(value = "people2")//3
    public Person findOne2(Person person) {
        Person p = personRepository.findOne(person.getId());
        logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    @Cacheable(value = "people3")//3
    public Person findOne3(Person person) {
        Person p = personRepository.findOne(person.getId());
        logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }
}

package com.xiaolyuh.service.impl;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import com.xiaolyuh.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    PersonRepository personRepository;

    @Override
    public Person save(Person person) {
        Person p = personRepository.save(person);
        logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

    @Override
    public void remove(Long id) {
        logger.info("删除了id、key为" + id + "的数据缓存");
        //这里不做实际删除操作
    }

    @Override
    public void removeAll() {
        logger.info("删除了所有缓存的数据缓存");
        //这里不做实际删除操作
    }

    @Override
    public Person findOne(Person person) throws Exception {
        Thread.sleep((long) (Math.random() * 500), 1000);
        Person p = personRepository.findOne(Example.of(person));
        logger.info("为id、key为: {} 数据做了缓存", p.getId());
        return p;
    }

    @Override
    public Person findOne1(Person person) throws Exception {
        Thread.sleep((long) (Math.random() * 500), 1000);
        Person p = personRepository.findOne(Example.of(person));
        logger.info("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }
}

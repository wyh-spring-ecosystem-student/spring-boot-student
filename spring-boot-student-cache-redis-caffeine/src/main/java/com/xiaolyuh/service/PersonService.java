package com.xiaolyuh.service;

import com.xiaolyuh.entity.Person;

import java.util.List;


public interface PersonService {
    Person save(Person person);

    void remove(Long id);

    Person findOne(Person person, String a, String[] b, List<Long> c);

    Person findOne1();

    Person findOne2(Person person);

    Person findOne3(Person person);
}

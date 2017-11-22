package com.xiaolyuh.service;

import com.xiaolyuh.entity.Person;


public interface PersonService {
    Person save(Person person);

    void remove(Long id);

    Person findOne(Person person);

    Person findOne1(Person person);

    Person findOne2(Person person);
}

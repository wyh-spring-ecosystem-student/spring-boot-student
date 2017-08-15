package com.xiaolyuh.service;

import com.xiaolyuh.entity.Person;


public interface PersonService {
    Person save(Person person);

    void remove(Long id);

    Person findOne(Person person);
}

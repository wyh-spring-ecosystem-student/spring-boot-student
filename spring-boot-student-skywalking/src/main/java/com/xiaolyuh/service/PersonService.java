package com.xiaolyuh.service;

import com.xiaolyuh.entity.Person;


public interface PersonService {
    Person save(Person person);

    void remove(Long id);

    void removeAll();

    Person findOne(Person person) throws InterruptedException, Exception;

    Person findOne1(Person person) throws InterruptedException, Exception;
}

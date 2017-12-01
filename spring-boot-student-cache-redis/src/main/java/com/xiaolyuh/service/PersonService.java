package com.xiaolyuh.service;

import com.xiaolyuh.entity.Person;

import java.math.BigDecimal;


public interface PersonService {
    Person save(Person person);

    void remove(Long id);

    Person findOne(Person person, String a, long b, boolean c, char d, BigDecimal bigDecimal);

    Person findOne1();

    Person findOne2(Person person);
}

package com.xiaolyuh.transaction.service;


import com.xiaolyuh.transaction.entity.Person;

import java.util.List;

/**
 * @author yuhao.wang3
 */
public interface PersonService {

    void insert(Person person);

    List<Person> findAll();
}

package com.xiaolyuh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiaolyuh.domain.mapper.PersonMapper;
import com.xiaolyuh.domain.model.Person;
import com.xiaolyuh.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yuhao.wang on 2017/6/19.
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;

    public static AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public List<Person> findAll() {
        personMapper.findAll();
        personMapper.findAll();
        personMapper.findAll();
        personMapper.findAll();
        personMapper.findAll();
        personMapper.findAll();
        personMapper.findAll();
        personMapper.findAll();
        personMapper.findAll();
        return personMapper.findAll();
    }

    @Override
    public Page<Person> findByPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return personMapper.findByPage();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(Person person) {
        personMapper.insert(person);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAge(long id) {
        int result = personMapper.updateAge(id);
        int i = atomicInteger.getAndIncrement();
        if (i > 990) {
            System.out.println(i);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        return result;
    }
}

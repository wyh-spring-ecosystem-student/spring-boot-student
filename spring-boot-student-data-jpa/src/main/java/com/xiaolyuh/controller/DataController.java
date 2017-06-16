package com.xiaolyuh.controller;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import com.xiaolyuh.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {
    // 1 Spring Data JPA已自动为你注册bean，所以可自动注入
    @Autowired
    PersonRepository personRepository;

    /**
     * 保存 save支持批量保存：<S extends T> Iterable<S> save(Iterable<S> entities);
     * <p>
     * 删除： 支持使用id删除对象、批量删除以及删除全部： void delete(ID id); void delete(T entity);
     * void delete(Iterable<? extends T> entities); void deleteAll();
     */
    @RequestMapping("/save")
    public Person save(@RequestBody Person person) {

        Person p = personRepository.save(person);

        return p;

    }

    /**
     * 测试findByAddress
     */
    @RequestMapping("/q1")
    public List<Person> q1(String address) {

        List<Person> people = personRepository.findByAddress(address);

        return people;

    }

    /**
     * 测试findByNameAndAddress
     */
    @RequestMapping("/q2")
    public Person q2(String name, String address) {

        Person people = personRepository.findByNameAndAddress(name, address);

        return people;

    }

    /**
     * 测试withNameAndAddressQuery
     */
    @RequestMapping("/q3")
    public Person q3(String name, String address) {

        Person p = personRepository.withNameAndAddressQuery(name, address);

        return p;

    }

    /**
     * 测试withNameAndAddressNamedQuery
     */
    @RequestMapping("/q4")
    public Person q4(String name, String address) {

        Person p = personRepository.withNameAndAddressNamedQuery(name, address);

        return p;

    }

    /**
     * 测试排序
     */
    @RequestMapping("/sort")
    public List<Person> sort() {

        List<Person> people = personRepository.findAll(new Sort(Direction.ASC, "age"));

        return people;

    }

    /**
     * 测试分页
     */
    @RequestMapping("/page")
    public Page<Person> page(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {

        Page<Person> pagePeople = personRepository.findAll(new PageRequest(pageNo, pageSize));

        return pagePeople;

    }

}
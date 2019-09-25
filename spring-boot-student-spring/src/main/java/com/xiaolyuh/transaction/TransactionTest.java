package com.xiaolyuh.transaction;

import com.alibaba.fastjson.JSON;
import com.xiaolyuh.BaseTest;
import com.xiaolyuh.PrintSpringBeanUtil;
import com.xiaolyuh.transaction.entity.Person;
import com.xiaolyuh.transaction.service.PersonService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 11:11
 */
public class TransactionTest extends BaseTest {

    @Before
    public void before() {
        super.before(TransactionConfig.class);
    }

    @Test
    public void contextTest() {

        PrintSpringBeanUtil.printlnBean(context);

        PersonService personService = context.getBean(PersonService.class);
        Person person = Person.builder().id(10L).address("成都天府广场").age(18).name("成都欢迎你").build();
        personService.insert(person);

        List<Person> persons = personService.findAll();
        System.out.println(persons.size());
        System.out.println(JSON.toJSONString(persons));
    }
}

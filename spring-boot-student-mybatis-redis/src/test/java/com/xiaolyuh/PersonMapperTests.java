package com.xiaolyuh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.xiaolyuh.domain.model.Person;
import com.xiaolyuh.holder.SpringContextHolder;
import com.xiaolyuh.service.PersonService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonMapperTests {

    private Logger logger = LoggerFactory.getLogger(PersonMapperTests.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    Person person = null;

    @Before
    public void testInsert() {
        person = new Person();
        person.setName("测试");
        person.setAddress("address");
        person.setAge(10);
        personService.insert(person);

        Assert.assertNotNull(person.getId());
        logger.debug(JSON.toJSONString(person));
    }

    @Test
    public void testFindAll() {
        List<Person> persons = personService.findAll();

        Assert.assertNotNull(persons);
        logger.debug(JSON.toJSONString(persons));
    }

    @Test
    public void testFindByPage() {
        Page<Person> persons = personService.findByPage(1, 2);

        Assert.assertNotNull(persons);
        logger.debug(persons.toString());
        logger.debug(JSON.toJSONString(persons));
    }

    // 测试mybatis缓存
    @Test
    public void testCache() {
        long begin = System.currentTimeMillis();
        List<Person> persons = personService.findAll();
        long ing = System.currentTimeMillis();
        personService.findAll();
        long end = System.currentTimeMillis();
        logger.debug("第一次请求时间：" + (ing - begin) + "ms");
        logger.debug("第二次请求时间:" + (end - ing) + "ms");

        Assert.assertNotNull(persons);
        logger.debug(JSON.toJSONString(persons));
    }

    // 测试Redis存储和获取一个List
    @SuppressWarnings("unchecked")
	@Test
    public void testRedisCacheSetList() {
        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person);
        persons.add(person);
        redisTemplate.opsForValue().set(person.getId() + "", persons, 2, TimeUnit.MINUTES);
        persons = (List<Person>) redisTemplate.opsForValue().get(person.getId() + "");
        System.out.println(JSON.toJSONString(persons));
    }

    // 测试Redis存储和获取一个Object
    @Test
    public void testRedisCacheSetObject() {
        redisTemplate.opsForValue().set(person.getId() + "", person, 2, TimeUnit.MINUTES);
        Object p = redisTemplate.opsForValue().get(person.getId() + "");
        if (p instanceof Person) {
            Person person1 = (Person) p;
            System.out.println(JSON.toJSONString(person1));
        }
    }

    // 测试 通过Spring Aware获取Spring容器中的额Bean
    @Test
    public void testApplicationContextAware() {
        RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
        System.out.println(redisTemplate);
    }

}

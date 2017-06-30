package com.xiaolyuh;

import com.github.pagehelper.Page;
import com.xiaolyuh.domain.model.Person;
import com.xiaolyuh.service.PersonService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonMapperTests {

	private Logger logger = LoggerFactory.getLogger(PersonMapperTests.class);

	@Autowired
	private PersonService personService;

	@Before
	public void testInsert() {
		Person person = new Person();
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

	@Test
	public void testCacheByPage() {
		long begin = System.currentTimeMillis();
		List<Person> persons = personService.findAll();
		long ing = System.currentTimeMillis();
		personService.findAll();
		long end = System.currentTimeMillis();
		logger.debug("第一次请求时间：" + (ing - begin) + "ms");
		logger.debug("第二次请求时间:" + (end - ing) + "ms");

		Assert.assertNotNull(persons);
		logger.debug(persons.toString());
		logger.debug(JSON.toJSONString(persons));
	}

}

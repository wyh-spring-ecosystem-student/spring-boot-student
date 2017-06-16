package com.xiaolyuh.service;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class PersonService {
	@Autowired
	PersonRepository personRepository;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	DataSourceProperties dataSourceProperties;

	/**
	 * 保存
	 * @param person
	 * @return
	 */
	@Transactional
	public Person save(	Person person) {
		// 获取配置的数据源
		DataSource dataSource = applicationContext.getBean(DataSource.class);
		// 查看配置数据源信息
		System.out.println(dataSource);
		System.out.println(dataSource.getClass().getName());
		System.out.println(dataSourceProperties);
		Person p = personRepository.save(person);

		return p;

	}
}

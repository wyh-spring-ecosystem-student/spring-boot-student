package com.xiaolyuh.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.xiaolyuh.entity.Person;

@Repository
public class PersonRepository {

	@Autowired
	StringRedisTemplate stringRedisTemplate; //1

	@Autowired
	RedisTemplate<Object, Object> redisTemplate; //2

	public void stringRedisTemplateDemo(){ //5
		stringRedisTemplate.opsForValue().set("xx", "yy");
	}


	public void save(Person person){ //6
		redisTemplate.opsForSet().add(person.getId(), person);
	}

	public String getString(){//7
		return stringRedisTemplate.opsForValue().get("xx");
	}

	public Person getPerson(){//8
		return (Person) redisTemplate.opsForValue().get("1");
	}

}

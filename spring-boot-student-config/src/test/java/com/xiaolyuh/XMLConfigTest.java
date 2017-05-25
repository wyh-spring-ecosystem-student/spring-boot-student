package com.xiaolyuh;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiaolyuh.entity.Person;

/**
 * Created by yuhao.wang on 2017/3/9.
 */
public class XMLConfigTest extends SpringBootStudentConfigApplicationTests {

	@Autowired
	Person person; 
	
	@Test
	public void contextTest() {
		System.out.println(person.toString());
	}

}

package com.xiaolyuh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiaolyuh.service.LogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootStudentLogApplicationTests {

	@Autowired
	private LogService logService;
	
	@Test
	public void contextLoads() {
		logService.log2();
	}

}

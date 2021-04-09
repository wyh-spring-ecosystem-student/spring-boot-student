package com.xiaolyuh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootStudentMybatisPlusApplicationTests {

	@Test
	public void contextLoads() {
		String s = "remote:   http://git.longhu.net/myteamgroup/gaia-hr-target/merge_requests/5";
		System.out.println(s.substring(s.lastIndexOf("   ")));
	}

}

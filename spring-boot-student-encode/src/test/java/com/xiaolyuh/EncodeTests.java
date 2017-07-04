package com.xiaolyuh;

import com.xiaolyuh.utils.AESUtil;
import com.xiaolyuh.utils.EncodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EncodeTests {

	@Test
	public void contextLoads() {
		String key = "123";
		System.out.println(EncodeUtil.sha(key));
		System.out.println(EncodeUtil.md5(key));
	}

}

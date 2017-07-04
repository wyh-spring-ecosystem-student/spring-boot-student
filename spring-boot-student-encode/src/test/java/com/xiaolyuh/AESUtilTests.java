package com.xiaolyuh;

import com.xiaolyuh.utils.AESUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AESUtilTests {

	@Test
	public void contextLoads() {
		String content = "test";
		String password = "12345678";
		// 加密
		System.out.println("加密前：" + content);
		byte[] encryptResult = AESUtil.encrypt(content, password);
		String encryptResultStr = AESUtil.parseByte2HexStr(encryptResult);
		System.out.println("加密后：" + encryptResultStr);
		// 解密
		byte[] decryptFrom = AESUtil.parseHexStr2Byte(encryptResultStr);
		byte[] decryptResult = AESUtil.decrypt(decryptFrom, password);
		System.out.println("解密后：" + new String(decryptResult));

		String enStr = AESUtil.encryptString("123", "@#&^%-$#@Coupon#$%^&@*");
		System.out.println(enStr);
		System.out.println(AESUtil.decrypt("2DA4A4EEA4777CB2CC342815FC84B539", "@#&^%-$#@Coupon#$%^&@*"));
	}

}

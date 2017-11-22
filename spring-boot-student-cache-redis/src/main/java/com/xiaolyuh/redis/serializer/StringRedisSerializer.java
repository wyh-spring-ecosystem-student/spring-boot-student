package com.xiaolyuh.redis.serializer;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

/**
 * 必须重写序列化器，否则@Cacheable注解的key会报类型转换错误
 * @authors yuhao.wang
 */
public class StringRedisSerializer implements RedisSerializer<Object> {

	private final Charset charset;

	public StringRedisSerializer() {
        this(Charset.forName("UTF8"));
	}

	public StringRedisSerializer(Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		this.charset = charset;
	}

	@Override
	public String deserialize(byte[] bytes) {
		return (bytes == null ? null : new String(bytes, charset));
	}

	@Override
	public byte[] serialize(Object object) {
		String string = null;
		if (object instanceof  String) {
			string = (String) object;
		} else if (object instanceof  Boolean || object instanceof  Long
				|| object instanceof  Integer || object instanceof  Double
				|| object instanceof  Float || object instanceof  Short
				|| object instanceof  Character || object instanceof Byte) {

			string = object.toString();
		} else {
			string = JSON.toJSONString(object);
		}
		return (string == null ? null : string.getBytes(charset));
	}
}

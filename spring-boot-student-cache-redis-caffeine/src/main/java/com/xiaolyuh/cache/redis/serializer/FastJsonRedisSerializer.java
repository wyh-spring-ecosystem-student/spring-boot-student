package com.xiaolyuh.cache.redis.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author yuhao.wang
 * @param <T>
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static final String EMPTY_OBJECT_FLAG = "EMPTY_OBJECT_FLAG_@$#";

    private Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null || t instanceof NullValue) {
            // 如果是NULL,则存空对象标示
            return EMPTY_OBJECT_FLAG.getBytes(DEFAULT_CHARSET);
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        // 判断存储对象是否是NULL，是就返回null
        if (EMPTY_OBJECT_FLAG.equals(str)) {
            return null;
        }
        return (T) JSON.parseObject(str, clazz);
    }

}
package com.xiaolyuh.cache.redis.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @param <T>
 * @author yuhao.wang
 */
public class KryoRedisSerializer<T> implements RedisSerializer<T> {
    Logger logger = LoggerFactory.getLogger(KryoRedisSerializer.class);

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static final String EMPTY_OBJECT_FLAG = "EMPTY_OBJECT_FLAG_@$#";

    private static final ThreadLocal<Kryo> kryos = ThreadLocal.withInitial(Kryo::new);

    private Class<T> clazz;

    public KryoRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null || t instanceof NullValue) {
            // 如果是NULL,则存空对象标示
            return EMPTY_OBJECT_FLAG.getBytes(DEFAULT_CHARSET);
        }

        Kryo kryo = kryos.get();
        kryo.setReferences(false);
        kryo.register(clazz);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, t);
        output.flush();
        output.close();

        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return b;
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(clazz);

        Input input = new Input(bytes);
        return (T) kryo.readClassAndObject(input);
    }

}
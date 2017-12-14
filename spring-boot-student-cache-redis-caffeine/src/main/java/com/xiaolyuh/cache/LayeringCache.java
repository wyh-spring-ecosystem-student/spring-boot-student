package com.xiaolyuh.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisOperations;

import java.util.concurrent.Callable;

/**
 * @author yuhao.wang
 */
public class LayeringCache extends AbstractValueAdaptingCache {

    Logger logger = LoggerFactory.getLogger(LayeringCache.class);

    /**
     * 缓存的名称
     */
    private final String name;

    /**
     * redi缓存
     */
    private RedisCache redisCache;

    /**
     * Caffeine缓存
     */
    private CaffeineCache caffeineCache;

    public LayeringCache(String name, RedisCache redisCache, CaffeineCache caffeineCache, boolean allowNullValues) {

        super(allowNullValues);
        this.name = name;
        this.redisCache = redisCache;
        this.caffeineCache = caffeineCache;
    }

    public LayeringCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                         long expiration, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        this(name, prefix, redisOperations, expiration, false, cache);
    }

    public LayeringCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                         long expiration, boolean allowNullValues, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        super(allowNullValues);
        this.name = name;
        this.redisCache = new RedisCache(name, prefix, redisOperations, expiration, allowNullValues);
        this.caffeineCache = new CaffeineCache(name, cache, allowNullValues);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper get(Object key) {
        // 查询一级缓存
        ValueWrapper wrapper = caffeineCache.get(key);
        logger.debug("查询一级缓存 key:{},返回值是:{}", key, wrapper);
        if (wrapper == null) {
            // 查询二级缓存
            wrapper = redisCache.get(key);
            logger.debug("查询二级缓存 key:{},返回值是:{}", key, wrapper);
        }
        return wrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        // 查询一级缓存
        T t = caffeineCache.get(key, type);
        logger.debug("查询一级缓存 key:{},返回值是:{}", key, t);
        if (t == null) {
            // 查询二级缓存
            t = redisCache.get(key, type);
            logger.debug("查询二级缓存 key:{},返回值是:{}", key, t);
        }
        return t;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        // 查询一级缓存
        T t = caffeineCache.get(key, valueLoader);
        logger.debug("查询一级缓存 key:{},返回值是:{}", key, t);
        if (t == null) {
            // 查询二级缓存
            t = redisCache.get(key, valueLoader);
        }
        return t;
    }

    @Override
    public void put(Object key, Object value) {
        caffeineCache.put(key, value);
        redisCache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        caffeineCache.putIfAbsent(key, value);
        return redisCache.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        caffeineCache.evict(key);
        redisCache.evict(key);
    }

    @Override
    public void clear() {
        caffeineCache.clear();
        redisCache.clear();
    }

    @Override
    protected Object lookup(Object key) {
        Object object = caffeineCache.get(key);
        if (object == null) {
            object = redisCache.get(key);
        }
        return object;
    }
}

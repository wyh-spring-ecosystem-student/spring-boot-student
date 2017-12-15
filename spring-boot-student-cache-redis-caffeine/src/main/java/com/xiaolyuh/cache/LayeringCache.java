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
    private final RedisCache redisCache;

    /**
     * Caffeine缓存
     */
    private final CaffeineCache caffeineCache;

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
        T value = caffeineCache.get(key, type);
        logger.debug("查询一级缓存 key:{},返回值是:{}", key, value);
        if (value == null) {
            // 查询二级缓存
            value = redisCache.get(key, type);
            caffeineCache.put(key, value);
            logger.debug("查询二级缓存 key:{},返回值是:{}", key, value);
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {

        System.out.println(caffeineCache.getNativeCache().asMap());
        // 查询一级缓存,如果一级缓存没有则调用getForsecondaryCache(k, valueLoader)查询二级缓存
        T value = (T) caffeineCache.getNativeCache().get(key, k -> getForSecondaryCache(k, valueLoader));
        return value;
    }

    /**
     * 查询二级缓存
     * @param key
     * @param valueLoader
     * @param <T>
     * @return
     */
    private <T> Object getForSecondaryCache(Object key, Callable<T> valueLoader) {
        T value = redisCache.get(key, valueLoader);
        logger.debug("查询二级缓存 key:{},返回值是:{}", key, value);

        return value;
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
        Object value = caffeineCache.get(key);
        logger.debug("查询一级缓存 key:{},返回值是:{}", key, value);
        if (value == null) {
            value = redisCache.get(key);
            logger.debug("查询二级缓存 key:{},返回值是:{}", key, value);
        }
        return value;
    }
}

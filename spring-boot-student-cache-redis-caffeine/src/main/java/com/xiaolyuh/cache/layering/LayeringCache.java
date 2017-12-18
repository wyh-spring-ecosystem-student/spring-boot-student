package com.xiaolyuh.cache.layering;

import com.xiaolyuh.cache.redis.cache.CustomizedRedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.cache.support.AbstractValueAdaptingCache;
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
     * 是否使用二级缓存
     */
    private boolean isUsedFirstCache = true;

    /**
     * redi缓存
     */
    private final CustomizedRedisCache redisCache;

    /**
     * Caffeine缓存
     */
    private final CaffeineCache caffeineCache;

    /**
     * @param name          缓存名称
     * @param redisCache    Redis缓存
     * @param caffeineCache Caffeine缓存
     */
    public LayeringCache(String name, CustomizedRedisCache redisCache, CaffeineCache caffeineCache, boolean allowNullValues, boolean isUsedFirstCache) {

        super(allowNullValues);
        this.name = name;
        this.redisCache = redisCache;
        this.caffeineCache = caffeineCache;
    }

    /**
     * @param name              缓存名称
     * @param prefix            缓存前缀
     * @param redisOperations   操作Redis的RedisTemplate
     * @param expiration        redis缓存过期时间
     * @param preloadSecondTime redis缓存自动刷新时间
     * @param caffeineCache     Caffeine缓存
     */
    public LayeringCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                         long expiration, long preloadSecondTime, com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache) {
        this(name, prefix, redisOperations, expiration, preloadSecondTime, false, true, caffeineCache);
    }

    /**
     * @param name              缓存名称
     * @param prefix            缓存前缀
     * @param redisOperations   操作Redis的RedisTemplate
     * @param expiration        redis缓存过期时间
     * @param preloadSecondTime redis缓存自动刷新时间
     * @param allowNullValues   是否允许存NULL，默认是false
     * @param caffeineCache     Caffeine缓存
     */
    public LayeringCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                         long expiration, long preloadSecondTime, boolean allowNullValues, boolean isUsedFirstCache,
                         com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache) {

        super(allowNullValues);
        this.name = name;
        this.isUsedFirstCache = isUsedFirstCache;
        this.redisCache = new CustomizedRedisCache(name, prefix, redisOperations, expiration, preloadSecondTime, allowNullValues);
        this.caffeineCache = new CaffeineCache(name, caffeineCache, allowNullValues);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    public CustomizedRedisCache getSecondaryCache() {
        return this.redisCache;
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper wrapper = null;
        if (isUsedFirstCache) {
            // 查询一级缓存
            wrapper = caffeineCache.get(key);
            logger.debug("查询一级缓存 key:{},返回值是:{}", key, wrapper);
        }

        if (wrapper == null) {
            // 查询二级缓存
            wrapper = redisCache.get(key);
            logger.debug("查询二级缓存 key:{},返回值是:{}", key, wrapper);
        }
        return wrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T value = null;
        if (isUsedFirstCache) {
            // 查询一级缓存
            value = caffeineCache.get(key, type);
            logger.debug("查询一级缓存 key:{},返回值是:{}", key, value);
        }

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
        T value = null;
        if (isUsedFirstCache) {
            // 查询一级缓存,如果一级缓存没有值则调用getForSecondaryCache(k, valueLoader)查询二级缓存
            value = (T) caffeineCache.getNativeCache().get(key, k -> getForSecondaryCache(k, valueLoader));
        } else {
            // 直接查询二级缓存
            value = (T) getForSecondaryCache(key, valueLoader);
        }
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

    /**
     * 查询二级缓存
     *
     * @param key
     * @param valueLoader
     * @return
     */
    private <T> Object getForSecondaryCache(Object key, Callable<T> valueLoader) {
        T value = redisCache.get(key, valueLoader);
        logger.debug("查询二级缓存 key:{},返回值是:{}", key, value);

        return value;
    }
}

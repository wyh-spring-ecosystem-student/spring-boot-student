package com.xiaolyuh.cache;

import com.github.benmanes.caffeine.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisOperations;

import java.util.concurrent.Callable;
import java.util.function.Function;

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
    private final com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache;

    public LayeringCache(String name, RedisCache redisCache, com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache,
                         boolean allowNullValues) {

        super(allowNullValues);
        this.name = name;
        this.redisCache = redisCache;
        this.caffeineCache = caffeineCache;
    }

    public LayeringCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                         long expiration, com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache) {
        this(name, prefix, redisOperations, expiration, false, caffeineCache);
    }

    public LayeringCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                         long expiration, boolean allowNullValues, com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache) {
        super(allowNullValues);
        this.name = name;
        this.redisCache = new RedisCache(name, prefix, redisOperations, expiration, allowNullValues);
        this.caffeineCache = caffeineCache;
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
        ValueWrapper wrapper = toValueWrapper(caffeineCache.get(key, k -> createCaffeineCache(k)));
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
//        T t = caffeineCache.get(key, type);
        T t = null;
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
//        T t = caffeineCache.get(key, k -> createCaffeineCache(k));
        T t = null;
        logger.debug("查询一级缓存 key:{},返回值是:{}", key, t);
        if (t == null) {
            // 查询二级缓存
            t = redisCache.get(key, valueLoader);
        }
        return t;
    }

    @Override
    public void put(Object key, Object value) {
        caffeineCache.put(key, toStoreValue(value));
        redisCache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        putIfAbsentCaffeineCache(key, value);
        return redisCache.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        caffeineCache.invalidate(key);
        redisCache.evict(key);
    }

    @Override
    public void clear() {
        caffeineCache.invalidateAll();
        redisCache.clear();
    }

    @Override
    protected Object lookup(Object key) {
        Object object = caffeineCache.get(key, k -> createCaffeineCache(k));
        if (object == null) {
            object = redisCache.get(key);
        }
        return object;
    }

    private Object createCaffeineCache(Object key) {
        return redisCache.get(key);
    }

    @Override
    protected Cache.ValueWrapper toValueWrapper(Object storeValue) {
        return (storeValue != null ? new SimpleValueWrapper(fromStoreValue(storeValue)) : null);
    }






//    @Override
//    public ValueWrapper get(Object key) {
//        if (this.cache instanceof LoadingCache) {
//            Object value = ((LoadingCache<Object, Object>) this.cache).get(key);
//            return toValueWrapper(value);
//        }
//        return super.get(key);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T> T get(Object key, final Callable<T> valueLoader) {
//        return (T) fromStoreValue(this.cache.get(key, new CaffeineCache.LoadFunction(valueLoader)));
//    }
//
//    @Override
//    protected Object lookup(Object key) {
//        return this.cache.getIfPresent(key);
//    }

    private ValueWrapper putIfAbsentCaffeineCache(Object key, final Object value) {
        LayeringCache.PutIfAbsentCaffeineCacheFunction callable = new LayeringCache.PutIfAbsentCaffeineCacheFunction(value);
        Object result = this.caffeineCache.get(key, callable);
        return (callable.called ? null : toValueWrapper(result));
    }

    private class PutIfAbsentCaffeineCacheFunction implements Function<Object, Object> {

        private final Object value;

        private boolean called;

        public PutIfAbsentCaffeineCacheFunction(Object value) {
            this.value = value;
        }

        @Override
        public Object apply(Object key) {
            this.called = true;
            return toStoreValue(this.value);
        }
    }


    private class LoadFunction implements Function<Object, Object> {

        private final Callable<?> valueLoader;

        public LoadFunction(Callable<?> valueLoader) {
            this.valueLoader = valueLoader;
        }

        @Override
        public Object apply(Object o) {
            try {
                return toStoreValue(valueLoader.call());
            }
            catch (Exception ex) {
                throw new ValueRetrievalException(o, valueLoader, ex);
            }
        }
    }






}

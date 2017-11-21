package com.xiaolyuh.redis.cache;

import com.xiaolyuh.redis.cache.helper.SpringContextHolder;
import com.xiaolyuh.redis.cache.helper.ThreadTaskHelper;
import com.xiaolyuh.redis.lock.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 自定义的redis缓存
 * @author yuhao.wang
 */
public class CustomizedRedisCache extends RedisCache {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedRedisCache.class);

    private CacheSupport getCacheSupport() {
        return SpringContextHolder.getBean(CacheSupport.class);
    }

    private final RedisOperations redisOperations;

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     * 单位：秒
     */
    private long preloadSecondTime = 0;

    public CustomizedRedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration, long preloadSecondTime) {
        super(name, prefix, redisOperations, expiration);
        this.redisOperations = redisOperations;
        this.preloadSecondTime = preloadSecondTime;
    }

    public CustomizedRedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration, long preloadSecondTime, boolean allowNullValues) {
        super(name, prefix, redisOperations, expiration, allowNullValues);
        this.redisOperations = redisOperations;
        this.preloadSecondTime = preloadSecondTime;
    }

    /**
     * 重写get方法，获取到缓存后再次取缓存剩余的时间，如果时间小余我们配置的刷新时间就手动刷新缓存。
     * 为了不影响get的性能，启用后台线程去完成缓存的刷。
     * 并且只放一个线程去刷新数据
     *
     * @param key
     * @return
     */
    @Override
    public ValueWrapper get(final Object key) {

        ValueWrapper valueWrapper = super.get(key);
        if (null != valueWrapper) {
            ThreadTaskHelper.run(new Runnable() {
                @Override
                public void run() {
                    Long ttl = CustomizedRedisCache.this.redisOperations.getExpire(key);
                    if (null != ttl && ttl <= CustomizedRedisCache.this.preloadSecondTime) {
                        // 加一个分布式锁，只放一个请求去刷新缓存
                        RedisLock redisLock = new RedisLock((RedisTemplate) redisOperations, key.toString() + "_lock");
                        try {
                            if (redisLock.lock()) {
                                //重新加载数据
                                logger.info("缓存：{}，重新加载数据", CustomizedRedisCache.super.getName());
                                CustomizedRedisCache.this.getCacheSupport().refreshCacheByKey(CustomizedRedisCache.super.getName(), key.toString());
                            }
                        } catch (Exception e) {
                            logger.info(e.getMessage(), e);
                        } finally {
                            redisLock.unlock();
                        }
                    }
                }
            });
        }
        return valueWrapper;
    }
}

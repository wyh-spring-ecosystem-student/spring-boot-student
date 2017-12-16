package com.xiaolyuh.redis.cache;

import com.xiaolyuh.redis.utils.ReflectionUtils;
import com.xiaolyuh.redis.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义的redis缓存管理器
 * 支持方法上配置过期时间
 * 支持热加载缓存：缓存即将过期时主动刷新缓存
 *
 * @author yuhao.wang
 */
public class CustomizedRedisCacheManager extends RedisCacheManager {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedRedisCacheManager.class);

    /**
     * 父类dynamic字段
     */
    private static final String SUPER_FIELD_DYNAMIC = "dynamic";

    /**
     * 父类cacheNullValues字段
     */
    private static final String SUPER_FIELD_CACHENULLVALUES = "cacheNullValues";

    RedisCacheManager redisCacheManager = null;

    // 0 - never expire
    private long defaultExpiration = 0;
    private Map<String, CacheTime> cacheTimes = null;

    public CustomizedRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }

    public CustomizedRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        super(redisOperations, cacheNames);
    }

    public RedisCacheManager getInstance() {
        if (redisCacheManager == null) {
            redisCacheManager = SpringContextUtils.getBean(RedisCacheManager.class);
        }
        return redisCacheManager;
    }

    /**
     * 获取过期时间
     *
     * @return
     */
    public long getExpirationSecondTime(String name) {
        if (StringUtils.isEmpty(name)) {
            return 0;
        }

        CacheTime cacheTime = null;
        if (!CollectionUtils.isEmpty(cacheTimes)) {
            cacheTime = cacheTimes.get(name);
        }
        Long expiration = cacheTime != null ? cacheTime.getExpirationSecondTime() : defaultExpiration;
        return expiration < 0 ? 0 : expiration;
    }

    /**
     * 获取自动刷新时间
     *
     * @return
     */
    private long getPreloadSecondTime(String name) {
        // 自动刷新时间，默认是0
        CacheTime cacheTime = null;
        if (!CollectionUtils.isEmpty(cacheTimes)) {
            cacheTime = cacheTimes.get(name);
        }
        Long preloadSecondTime = cacheTime != null ? cacheTime.getPreloadSecondTime() : 0;
        return preloadSecondTime < 0 ? 0 : preloadSecondTime;
    }

    /**
     * 创建缓存
     *
     * @param cacheName 缓存名称
     * @return
     */
    public CustomizedRedisCache getMissingCache(String cacheName) {

        // 有效时间，初始化获取默认的有效时间
        Long expirationSecondTime = getExpirationSecondTime(cacheName);
        // 自动刷新时间，默认是0
        Long preloadSecondTime = getPreloadSecondTime(cacheName);

        logger.info("缓存 cacheName：{}，过期时间:{}, 自动刷新时间:{}", cacheName, expirationSecondTime, preloadSecondTime);
        // 是否在运行时创建Cache
        Boolean dynamic = (Boolean) ReflectionUtils.getFieldValue(getInstance(), SUPER_FIELD_DYNAMIC);
        // 是否允许存放NULL
        Boolean cacheNullValues = (Boolean) ReflectionUtils.getFieldValue(getInstance(), SUPER_FIELD_CACHENULLVALUES);
        return dynamic ? new CustomizedRedisCache(cacheName, (this.isUsePrefix() ? this.getCachePrefix().prefix(cacheName) : null),
                this.getRedisOperations(), expirationSecondTime, preloadSecondTime, cacheNullValues) : null;
    }

    /**
     * 根据缓存名称设置缓存的有效时间和刷新时间，单位秒
     *
     * @param cacheTimes
     */
    public void setCacheTimess(Map<String, CacheTime> cacheTimes) {
        this.cacheTimes = (cacheTimes != null ? new ConcurrentHashMap<String, CacheTime>(cacheTimes) : null);
    }

    /**
     * 设置默认的过去时间， 单位：秒
     *
     * @param defaultExpireTime
     */
    @Override
    public void setDefaultExpiration(long defaultExpireTime) {
        super.setDefaultExpiration(defaultExpireTime);
        this.defaultExpiration = defaultExpireTime;
    }

    @Deprecated
    @Override
    public void setExpires(Map<String, Long> expires) {

    }
}

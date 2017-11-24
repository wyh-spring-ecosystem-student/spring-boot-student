package com.xiaolyuh.redis.cache;

import com.xiaolyuh.redis.cache.helper.SpringContextHolder;
import com.xiaolyuh.redis.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Collection;
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
     * 父类cacheMap字段
     */
    private static final String SUPER_FIELD_CACHEMAP = "cacheMap";

    /**
     * 父类dynamic字段
     */
    private static final String SUPER_FIELD_DYNAMIC = "dynamic";

    /**
     * 父类cacheNullValues字段
     */
    private static final String SUPER_FIELD_CACHENULLVALUES = "cacheNullValues";

    /**
     * 父类updateCacheNames方法
     */
    private static final String SUPER_METHOD_UPDATECACHENAMES = "updateCacheNames";

    /**
     * 缓存参数的分隔符
     * 数组元素0=缓存的名称
     * 数组元素1=缓存过期时间TTL
     * 数组元素2=缓存在多少秒开始主动失效来强制刷新
     */
    private static final String SEPARATOR = "#";

    /**
     * SpEL标示符
     */
    private static final String MARK = "$";

    RedisCacheManager redisCacheManager = null;

    @Autowired
    DefaultListableBeanFactory beanFactory;

    public CustomizedRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }

    public CustomizedRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        super(redisOperations, cacheNames);
    }

    public RedisCacheManager getInstance() {
        if (redisCacheManager == null) {
            redisCacheManager = SpringContextHolder.getBean(RedisCacheManager.class);
        }
        return redisCacheManager;
    }

    @Override
    public Cache getCache(String name) {
        String[] cacheParams = name.split(SEPARATOR);
        String cacheName = cacheParams[0];

        if (StringUtils.isBlank(cacheName)) {
            return null;
        }

        // 有效时间，初始化获取默认的有效时间
        Long expirationSecondTime = getExpirationSecondTime(cacheName, cacheParams);
        // 自动刷新时间，默认是0
        Long preloadSecondTime = getExpirationSecondTime(cacheParams);

        // 通过反射获取父类存放缓存的容器对象
        Object object = ReflectionUtils.getFieldValue(getInstance(), SUPER_FIELD_CACHEMAP);
        if (object != null && object instanceof ConcurrentHashMap) {
            ConcurrentHashMap<String, Cache> cacheMap = (ConcurrentHashMap<String, Cache>) object;
            // 生成Cache对象，并将其保存到父类的Cache容器中
            return getCache(cacheName, expirationSecondTime, preloadSecondTime, cacheMap);
        } else {
            return super.getCache(cacheName);
        }

    }

    /**
     * 获取过期时间
     *
     * @return
     */
    private long getExpirationSecondTime(String cacheName, String[] cacheParams) {
        // 有效时间，初始化获取默认的有效时间
        Long expirationSecondTime = this.computeExpiration(cacheName);

        // 设置key有效时间
        if (cacheParams.length > 1) {
            String expirationStr = cacheParams[1];
            if (!StringUtils.isEmpty(expirationStr)) {
                // 支持配置过期时间使用EL表达式读取配置文件时间
                if (expirationStr.contains(MARK)) {
                    expirationStr = beanFactory.resolveEmbeddedValue(expirationStr);
                }
                expirationSecondTime = Long.parseLong(expirationStr);
            }
        }

        return expirationSecondTime;
    }

    /**
     * 获取自动刷新时间
     *
     * @return
     */
    private long getExpirationSecondTime(String[] cacheParams) {
        // 自动刷新时间，默认是0
        Long preloadSecondTime = 0L;
        // 设置自动刷新时间
        if (cacheParams.length > 2) {
            String preloadStr = cacheParams[2];
            if (!StringUtils.isEmpty(preloadStr)) {
                // 支持配置刷新时间使用EL表达式读取配置文件时间
                if (preloadStr.contains(MARK)) {
                    preloadStr = beanFactory.resolveEmbeddedValue(preloadStr);
                }
                preloadSecondTime = Long.parseLong(preloadStr);
            }
        }
        return preloadSecondTime;
    }

    /**
     * 重写父类的getCache方法，真假了三个参数
     *
     * @param cacheName            缓存名称
     * @param expirationSecondTime 过期时间
     * @param preloadSecondTime    自动刷新时间
     * @param cacheMap             通过反射获取的父类的cacheMap对象
     * @return Cache
     */
    public Cache getCache(String cacheName, long expirationSecondTime, long preloadSecondTime, ConcurrentHashMap<String, Cache> cacheMap) {
        Cache cache = cacheMap.get(cacheName);
        if (cache != null) {
            return cache;
        } else {
            // Fully synchronize now for missing cache creation...
            synchronized (cacheMap) {
                cache = cacheMap.get(cacheName);
                if (cache == null) {
                    // 调用我们自己的getMissingCache方法创建自己的cache
                    cache = getMissingCache(cacheName, expirationSecondTime, preloadSecondTime);
                    if (cache != null) {
                        cache = decorateCache(cache);
                        cacheMap.put(cacheName, cache);

                        // 反射去执行父类的updateCacheNames(cacheName)方法
                        Class<?>[] parameterTypes = {String.class};
                        Object[] parameters = {cacheName};
                        ReflectionUtils.invokeMethod(getInstance(), SUPER_METHOD_UPDATECACHENAMES, parameterTypes, parameters);
                    }
                }
                return cache;
            }
        }
    }

    /**
     * 创建缓存
     *
     * @param cacheName            缓存名称
     * @param expirationSecondTime 过期时间
     * @param preloadSecondTime    制动刷新时间
     * @return
     */
    public CustomizedRedisCache getMissingCache(String cacheName, long expirationSecondTime, long preloadSecondTime) {

        logger.info("缓存 cacheName：{}，过期时间:{}, 自动刷新时间:{}", cacheName, expirationSecondTime, preloadSecondTime);
        Boolean dynamic = (Boolean) ReflectionUtils.getFieldValue(getInstance(), SUPER_FIELD_DYNAMIC);
        Boolean cacheNullValues = (Boolean) ReflectionUtils.getFieldValue(getInstance(), SUPER_FIELD_CACHENULLVALUES);
        return dynamic ? new CustomizedRedisCache(cacheName, (this.isUsePrefix() ? this.getCachePrefix().prefix(cacheName) : null),
                this.getRedisOperations(), expirationSecondTime, preloadSecondTime, cacheNullValues) : null;
    }
}

package com.xiaolyuh.cache.layering;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.xiaolyuh.cache.setting.FirstCacheSetting;
import com.xiaolyuh.cache.setting.SecondaryCacheSetting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yuhao.wang
 */
@SuppressWarnings("rawtypes")
public class LayeringCacheManager implements CacheManager {
    // 常量
    static final int DEFAULT_EXPIRE_AFTER_WRITE = 2;
    static final int DEFAULT_INITIAL_CAPACITY = 5;
    static final int DEFAULT_MAXIMUM_SIZE = 1_000;

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(16);


    /**
     * 一级缓存配置
     */
    private Map<String, FirstCacheSetting> firstCacheSettings = null;

    /**
     * 二级缓存配置
     */
    private Map<String, SecondaryCacheSetting> secondaryCacheSettings = null;

    /**
     * 是否允许动态创建缓存，默认是true
     */
    private boolean dynamic = true;

    /**
     * 缓存值是否允许为NULL
     */
    private boolean allowNullValues = false;

    // Caffeine 属性
    /**
     * expireAfterWrite：2 小时
     * initialCapacity：5
     * maximumSize： 1_000
     */
    private Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder()
            .expireAfterAccess(DEFAULT_EXPIRE_AFTER_WRITE, TimeUnit.HOURS)
            .initialCapacity(DEFAULT_INITIAL_CAPACITY)
            .maximumSize(DEFAULT_MAXIMUM_SIZE);

    // redis 属性
    /**
     * 操作redis的RedisTemplate
     */
    private final RedisOperations redisOperations;

    /**
     * 二级缓存使用使用前缀，默认是false，建议设置成true
     */
    private boolean usePrefix = false;
    private RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix();

    /**
     * redis缓存默认时间，默认是0 永不过期
     */
    private long defaultExpiration = 0;

    public LayeringCacheManager(RedisOperations redisOperations) {
        this(redisOperations, Collections.<String>emptyList());
    }

    public LayeringCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        this(redisOperations, cacheNames, false);
    }

    public LayeringCacheManager(RedisOperations redisOperations, Collection<String> cacheNames, boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
        this.redisOperations = redisOperations;

        setCacheNames(cacheNames);
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createCache(name);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    @SuppressWarnings("unchecked")
    protected Cache createCache(String name) {
        return new LayeringCache(name, (usePrefix ? cachePrefix.prefix(name) : null), redisOperations,
                getSecondaryCacheExpirationSecondTime(name), getSecondaryCachePreloadSecondTime(name),
                isAllowNullValues(), getUsedFirstCache(name), getForceRefresh(name), createNativeCaffeineCache(name));
    }

    /**
     * Create a native Caffeine Cache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the native Caffeine Cache instance
     */
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
        return getCaffeine(name).build();
    }

    /**
     * 使用该CacheManager的当前状态重新创建已知的缓存。
     */
    private void refreshKnownCaches() {
        for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
            entry.setValue(createCache(entry.getKey()));
        }
    }

    /**
     * 在初始化CacheManager的时候初始化一组缓存。
     * 使用这个方法会在CacheManager初始化的时候就会将一组缓存初始化好，并且在运行时不会再去创建更多的缓存。
     * 使用空的Collection或者重新在配置里面指定dynamic后，就可重新在运行时动态的来创建缓存。
     *
     * @param cacheNames
     */
    public void setCacheNames(Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createCache(name));
            }
            this.dynamic = cacheNames.isEmpty();
        }
    }

    /**
     * 设置是否允许Cache的值为null
     *
     * @param allowNullValues
     */
    public void setAllowNullValues(boolean allowNullValues) {
        if (this.allowNullValues != allowNullValues) {
            this.allowNullValues = allowNullValues;
            refreshKnownCaches();
        }
    }

    /**
     * 获取是否允许Cache的值为null
     *
     * @return
     */
    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    /**
     * 在生成key的时候是否是否使用缓存名称来作为缓存前缀。默认是false，但是建议设置成true。
     *
     * @param usePrefix
     */
    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    protected boolean isUsePrefix() {
        return usePrefix;
    }

    /**
     * 设置redis默认的过期时间（单位：秒）
     *
     * @param defaultExpireTime
     */
    public void setSecondaryCacheDefaultExpiration(long defaultExpireTime) {
        this.defaultExpiration = defaultExpireTime;
    }


    /**
     * 根据缓存名称设置一级缓存的有效时间和刷新时间，单位秒
     *
     * @param firstCacheSettings
     */
    public void setFirstCacheSettings(Map<String, FirstCacheSetting> firstCacheSettings) {
        this.firstCacheSettings = (!CollectionUtils.isEmpty(firstCacheSettings) ? new ConcurrentHashMap<>(firstCacheSettings) : null);
    }

    /**
     * 根据缓存名称设置二级缓存的有效时间和刷新时间，单位秒
     *
     * @param secondaryCacheSettings
     */
    public void setSecondaryCacheSettings(Map<String, SecondaryCacheSetting> secondaryCacheSettings) {
        this.secondaryCacheSettings = (!CollectionUtils.isEmpty(secondaryCacheSettings) ? new ConcurrentHashMap<>(secondaryCacheSettings) : null);
    }


    /**
     * 获取过期时间
     *
     * @return
     */
    public long getSecondaryCacheExpirationSecondTime(String name) {
        if (StringUtils.isEmpty(name)) {
            return 0;
        }

        SecondaryCacheSetting secondaryCacheSetting = null;
        if (!CollectionUtils.isEmpty(secondaryCacheSettings)) {
            secondaryCacheSetting = secondaryCacheSettings.get(name);
        }
        Long expiration = secondaryCacheSetting != null ? secondaryCacheSetting.getExpirationSecondTime() : defaultExpiration;
        return expiration < 0 ? 0 : expiration;
    }

    /**
     * 获取自动刷新时间
     *
     * @return
     */
    private long getSecondaryCachePreloadSecondTime(String name) {
        // 自动刷新时间，默认是0
        SecondaryCacheSetting secondaryCacheSetting = null;
        if (!CollectionUtils.isEmpty(secondaryCacheSettings)) {
            secondaryCacheSetting = secondaryCacheSettings.get(name);
        }
        Long preloadSecondTime = secondaryCacheSetting != null ? secondaryCacheSetting.getPreloadSecondTime() : 0;
        return preloadSecondTime < 0 ? 0 : preloadSecondTime;
    }

    /**
     * 获取是否使用二级缓存，默认是true
     */
    public boolean getUsedFirstCache(String name) {
        SecondaryCacheSetting secondaryCacheSetting = null;
        if (!CollectionUtils.isEmpty(secondaryCacheSettings)) {
            secondaryCacheSetting = secondaryCacheSettings.get(name);
        }

        return secondaryCacheSetting != null ? secondaryCacheSetting.getUsedFirstCache() : true;
    }

    /**
     * 获取是否强制刷新（走数据库），默认是false
     */
    public boolean getForceRefresh(String name) {
        SecondaryCacheSetting secondaryCacheSetting = null;
        if (!CollectionUtils.isEmpty(secondaryCacheSettings)) {
            secondaryCacheSetting = secondaryCacheSettings.get(name);
        }

        return secondaryCacheSetting != null ? secondaryCacheSetting.getForceRefresh() : false;
    }

    public void setCaffeineSpec(CaffeineSpec caffeineSpec) {
        Caffeine<Object, Object> cacheBuilder = Caffeine.from(caffeineSpec);
        if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder)) {
            this.cacheBuilder = cacheBuilder;
            refreshKnownCaches();
        }
    }

    private Caffeine<Object, Object> getCaffeine(String name) {
        if (!CollectionUtils.isEmpty(firstCacheSettings)) {
            FirstCacheSetting firstCacheSetting = firstCacheSettings.get(name);
            if (firstCacheSetting != null && StringUtils.isNotBlank(firstCacheSetting.getCacheSpecification())) {
                // 根据缓存名称获取一级缓存配置
                return Caffeine.from(CaffeineSpec.parse(firstCacheSetting.getCacheSpecification()));
            }
        }

        return this.cacheBuilder;
    }
}

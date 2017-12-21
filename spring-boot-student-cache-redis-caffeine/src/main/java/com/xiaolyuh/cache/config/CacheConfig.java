package com.xiaolyuh.cache.config;

import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.xiaolyuh.cache.layering.LayeringCacheManager;
import com.xiaolyuh.cache.setting.FirstCacheSetting;
import com.xiaolyuh.cache.setting.SecondaryCacheSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuhao.wang
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {

    // redis缓存的有效时间单位是秒
    @Value("${redis.default.expiration:3600}")
    private long redisDefaultExpiration;

    // 查询缓存有效时间
    @Value("${select.cache.timeout:1800}")
    private long selectCacheTimeout;
    // 查询缓存自动刷新时间
    @Value("${select.cache.refresh:1790}")
    private long selectCacheRefresh;

    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    @Primary
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        LayeringCacheManager layeringCacheManager = new LayeringCacheManager(redisTemplate);
        // Caffeine缓存设置
        setFirstCacheConfig(layeringCacheManager);

        // redis缓存设置
        setSecondaryCacheConfig(layeringCacheManager);
        return layeringCacheManager;
    }

    private void setFirstCacheConfig(LayeringCacheManager layeringCacheManager) {
        // 设置默认的一级缓存配置
        String specification = this.cacheProperties.getCaffeine().getSpec();
        if (StringUtils.hasText(specification)) {
            layeringCacheManager.setCaffeineSpec(CaffeineSpec.parse(specification));
        }

        // 设置每个一级缓存的过期时间和自动刷新时间
        Map<String, FirstCacheSetting> firstCacheSettings = new HashMap<>();
        firstCacheSettings.put("people", new FirstCacheSetting("initialCapacity=5,maximumSize=500,expireAfterWrite=10s"));
        firstCacheSettings.put("people1", new FirstCacheSetting("initialCapacity=5,maximumSize=50,expireAfterAccess=10s"));
        layeringCacheManager.setFirstCacheSettings(firstCacheSettings);
    }

    private void setSecondaryCacheConfig(LayeringCacheManager layeringCacheManager) {
        // 设置使用缓存名称（value属性）作为redis缓存前缀
        layeringCacheManager.setUsePrefix(true);
        //这里可以设置一个默认的过期时间 单位是秒
        layeringCacheManager.setSecondaryCacheDefaultExpiration(redisDefaultExpiration);

        // 设置每个二级缓存的过期时间和自动刷新时间
        Map<String, SecondaryCacheSetting> secondaryCacheSettings = new HashMap<>();
        secondaryCacheSettings.put("people", new SecondaryCacheSetting(selectCacheTimeout, selectCacheRefresh));
        secondaryCacheSettings.put("people1", new SecondaryCacheSetting(selectCacheTimeout, selectCacheRefresh, true));
        secondaryCacheSettings.put("people2", new SecondaryCacheSetting(false, selectCacheTimeout, selectCacheRefresh));
        secondaryCacheSettings.put("people3", new SecondaryCacheSetting(selectCacheTimeout, selectCacheRefresh, false, true));
        layeringCacheManager.setSecondaryCacheSettings(secondaryCacheSettings);
    }

    /**
     * 显示声明缓存key生成器
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {

        return new SimpleKeyGenerator();
    }

}

package com.xiaolyuh.cache.config;

import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.xiaolyuh.cache.layering.LayeringCacheManager;
import com.xiaolyuh.cache.redis.cache.CacheTime;
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
    @Value("${select.cache.refresh:1799}")
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
        String specification = this.cacheProperties.getCaffeine().getSpec();
        if (StringUtils.hasText(specification)) {
            layeringCacheManager.setCaffeineSpec(CaffeineSpec.parse(specification));
        }
    }

    private void setSecondaryCacheConfig(LayeringCacheManager layeringCacheManager) {
        // 设置使用缓存名称（value属性）作为redis缓存前缀
        layeringCacheManager.setUsePrefix(true);
        //这里可以设置一个默认的过期时间 单位是秒
        layeringCacheManager.setSecondaryCacheDefaultExpiration(redisDefaultExpiration);
        // 设置缓存的过期时间和自动刷新时间
        Map<String, CacheTime> cacheTimes = new HashMap<>();
        cacheTimes.put("people", new CacheTime(selectCacheTimeout, selectCacheRefresh));
        cacheTimes.put("people1", new CacheTime(120, 115));
        cacheTimes.put("people2", new CacheTime(120, 115));
        layeringCacheManager.setSecondaryCacheTimess(cacheTimes);
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

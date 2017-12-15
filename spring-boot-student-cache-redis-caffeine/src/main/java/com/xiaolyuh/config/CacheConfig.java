package com.xiaolyuh.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.xiaolyuh.cache.LayeringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author yuhao.wang
 */
@Configuration
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        LayeringCacheManager layeringCacheManager = new LayeringCacheManager(redisTemplate);
        // 设置使用缓存名称（value属性）作为redis缓存前缀
        layeringCacheManager.setUsePrefix(true);
        //这里可以设置一个默认的过期时间 单位是秒
        layeringCacheManager.setSecondaryCacheDefaultExpiration(600);

        return layeringCacheManager;
    }

    @Bean
    public CaffeineCacheManager caffeineCacheManager(RedisTemplate<String, Object> redisTemplate) {
        CaffeineCacheManager redisCacheManager = new CaffeineCacheManager();

        return redisCacheManager;
    }

}

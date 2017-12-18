package com.xiaolyuh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import com.xiaolyuh.cache.layering.LayeringCacheManager;

/**
 * @author yuhao.wang
 */
@Configuration
public class CacheConfig {


    // redis缓存的有效时间单位是秒
    @Value("${redis.default.expiration:3600}")
    private long redisDefaultExpiration;


    /**
     * 显示声明缓存key生成器
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {

        return new SimpleKeyGenerator();
    }


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

}

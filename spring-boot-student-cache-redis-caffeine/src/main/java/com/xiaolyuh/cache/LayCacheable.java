package com.xiaolyuh.cache;

import com.xiaolyuh.cache.setting.FirstCacheSetting;
import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LayCacheable {

    String[] value() default {};

    String[] cacheNames() default {};

    String key() default "";

    String keyGenerator() default "";

    String cacheManager() default "";


    String cacheResolver() default "";

    String condition() default "";

    String unless() default "";

    boolean sync() default false;

    Class fcs() default FirstCacheSetting.class;

    Cacheable ccc();
}
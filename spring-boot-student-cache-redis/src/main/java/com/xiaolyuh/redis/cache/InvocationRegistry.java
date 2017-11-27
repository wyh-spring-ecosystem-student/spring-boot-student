package com.xiaolyuh.redis.cache;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 缓存方法注册接口
 *
 * @author yuhao.wang
 */
public interface InvocationRegistry {

    /**
     * @param invokedBean         代理Bean
     * @param invokedMethod       代理方法名称
     * @param invocationParamTypes 代理方法参数类型
     * @param invocationArgs 代理方法参数
     * @param cacheNames          缓存名称（@Cacheable注解的value）
     * @param cacheKey            缓存key（@Cacheable注解的key）
     */
    void registerInvocation(Object invokedBean, Method invokedMethod, Class[] invocationParamTypes, Object[] invocationArgs, Set<String> cacheNames, String cacheKey);

}
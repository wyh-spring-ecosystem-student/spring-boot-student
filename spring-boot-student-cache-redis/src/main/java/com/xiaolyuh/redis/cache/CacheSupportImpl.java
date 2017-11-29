package com.xiaolyuh.redis.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaolyuh.redis.cache.expression.CacheOperationExpressionEvaluator;
import com.xiaolyuh.redis.cache.helper.SpringContextHolder;
import com.xiaolyuh.redis.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 手动刷新缓存实现类
 *
 * @author yuhao.wang
 */
@Component
public class CacheSupportImpl implements CacheSupport, InvocationRegistry {
    private static final Logger logger = LoggerFactory.getLogger(CacheSupportImpl.class);

    private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();

    private final String SEPARATOR = "#";

    private final String INVOCATION_CACHE_KEY_SUFFIX = ":invocation_cache_key_suffix";

    @Autowired
    private KeyGenerator keyGenerator;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CustomizedRedisCacheManager cacheManager;

    private void refreshCache(CachedInvocation invocation, String cacheName) {

        boolean invocationSuccess;
        Object computed = null;
        try {
            // 通过代理调用方法，并记录返回值
            computed = invoke(invocation);
            invocationSuccess = true;
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
            invocationSuccess = false;
        }
        if (invocationSuccess) {
            // 通过cacheManager获取操作缓存的cache对象
            Cache cache = cacheManager.getCache(cacheName);
            // 通过Cache对象更新缓存
            cache.put(invocation.getKey(), computed);

            CustomizedRedisCache redisCache = (CustomizedRedisCache) cache;
            long expireTime = redisCache.getExpirationSecondTime();
            // 刷新redis中缓存法信息key的有效时间
            redisTemplate.expire(getInvocationCacheKey(redisCache.getCacheKey(invocation.getKey())), redisCache.getExpirationSecondTime(), TimeUnit.SECONDS);

            logger.info("缓存：{}-{}，重新加载数据", cacheName, invocation.getKey().toString().getBytes());
        }
    }

    private Object invoke(CachedInvocation invocation) throws Exception {

        // 获取执行方法所需要的参数
        Object[] args = null;
        if (!CollectionUtils.isEmpty(invocation.getParameterTypes())) {
            args = new Object[invocation.getParameterTypes().size()];
            for (int i = 0; i < invocation.getParameterTypes().size(); i++) {
                // 将参数转换成对应类型
                args[i] = JSON.parseObject(invocation.getArguments().get(i).toString(), invocation.getParameterTypes().get(i));
            }
        }
        // 通过先获取Spring的代理对象，在根据这个对象获取真实的实例对象
        Object target = ReflectionUtils.getTarget(SpringContextHolder.getBean(invocation.getTargetBean()));

        final MethodInvoker invoker = new MethodInvoker();
        invoker.setTargetObject(target);
        invoker.setArguments(args);
        invoker.setTargetMethod(invocation.getTargetMethod());
        invoker.prepare();

        return invoker.invoke();
    }

    @Override
    public void registerInvocation(Object targetBean, Method targetMethod, Class[] invocationParamTypes,
                                   Object[] invocationArgs, Set<String> annotatedCacheNames, String cacheKey) {


        Field[] fields = targetBean.getClass().getDeclaredFields();

        for (Field field: fields
             ) {

        }


        // 获取注解上真实的value值
        Collection<String> cacheNames = generateValue(annotatedCacheNames);

        // 获取注解上的key属性值
        Class<?> targetClass = getTargetClass(targetBean);
        Collection<? extends Cache> caches = getCache(cacheNames);
        Object key = generateKey(caches, cacheKey, targetMethod, invocationArgs, targetBean, targetClass,
                CacheOperationExpressionEvaluator.NO_RESULT);

        // 新建一个代理对象（记录了缓存注解的方法类信息）
        final CachedInvocation invocation = new CachedInvocation(key, targetBean, targetMethod.getName(), invocationParamTypes, invocationArgs);
        for (Cache cache: caches) {
            if(cache instanceof CustomizedRedisCache) {
                CustomizedRedisCache redisCache = ((CustomizedRedisCache) cache);
                // 将方法信息放到redis缓存
                redisTemplate.opsForValue().set(getInvocationCacheKey(redisCache.getCacheKey(key)), JSON.toJSON(invocation), redisCache.getExpirationSecondTime(), TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void refreshCache(String cacheName) {
        this.refreshCacheByKey(cacheName, null);
    }

    @Override
    public void refreshCacheByKey(String cacheName, String cacheKey) {
        //在redis拿到方法信息，然后刷新缓存
        JSONObject json = (JSONObject) redisTemplate.opsForValue().get(getInvocationCacheKey(cacheKey));
        if (json != null && !json.isEmpty()) {
            CachedInvocation invocation = JSON.parseObject(json.toJSONString(), CachedInvocation.class);
            // 执行刷新方法
            refreshCache(invocation, cacheName);
        }

    }

    /**
     * 获取注解上的value属性值（cacheNames）
     *
     * @param annotatedCacheNames
     * @return
     */
    private Collection<String> generateValue(Set<String> annotatedCacheNames) {
        Collection<String> cacheNames = new HashSet<>();
        for (final String cacheName : annotatedCacheNames) {
            String[] cacheParams = cacheName.split(SEPARATOR);
            // 截取名称获取真实的value值
            String realCacheName = cacheParams[0];
            cacheNames.add(realCacheName);
        }
        return cacheNames;
    }

    /**
     * 解析SpEL表达式，获取注解上的key属性值
     * 直接扣的Spring解析表达式部分代码
     *
     * @return
     */
    protected Object generateKey(Collection<? extends Cache> caches, String key, Method method, Object[] args,
                                 Object target, Class<?> targetClass, Object result) {

        if (org.springframework.util.StringUtils.hasText(key)) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(caches, method, args, target,
                    targetClass, result, null);

            AnnotatedElementKey methodCacheKey = new AnnotatedElementKey(method, targetClass);
            return evaluator.key(key, methodCacheKey, evaluationContext);
        }
        return this.keyGenerator.generate(target, method, args);
    }

    /**
     * 获取类信息
     *
     * @param target
     * @return
     */
    private Class<?> getTargetClass(Object target) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        if (targetClass == null && target != null) {
            targetClass = target.getClass();
        }
        return targetClass;
    }

    /**
     * 通过cache名称获取cache列表
     *
     * @param cacheNames
     * @return
     */
    public Collection<? extends Cache> getCache(Collection<String> cacheNames) {
        if (cacheNames == null) {
            return Collections.emptyList();
        } else {
            Collection<Cache> result = new ArrayList<Cache>();
            for (String cacheName : cacheNames) {
                Cache cache = this.cacheManager.getCache(cacheName);
                if (cache == null) {
                    throw new IllegalArgumentException("Cannot find cache named '" + cacheName + "' for ");
                }
                result.add(cache);
            }
            return result;
        }
    }

    private String getInvocationCacheKey(String cacheKey) {
        return cacheKey + INVOCATION_CACHE_KEY_SUFFIX;
    }

}

package com.xiaolyuh.redis.cache;

import com.xiaolyuh.redis.cache.expression.CacheOperationExpressionEvaluator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 手动刷新缓存实现类
 * @author yuhao.wang
 */
@Component
public class CacheSupportImpl implements CacheSupport, InvocationRegistry {
	private static final Logger logger = LoggerFactory.getLogger(CacheSupportImpl.class);

	private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();

	@Autowired
	private KeyGenerator keyGenerator;

	private final String SEPARATOR = "#";

	/**
	 * 记录缓存执行方法信息的容器。
	 * 如果有很多无用的缓存数据的话，有可能会照成内存溢出。
	 */
	private Map<String, Set<CachedInvocation>> cacheToInvocationsMap = new ConcurrentHashMap<>();

	@Autowired
	private CacheManager cacheManager;

	private void refreshCache(CachedInvocation invocation, String cacheName) {

		boolean invocationSuccess;
		Object computed = null;
		try {
			// 通过代理调用方法，并记录返回值
			computed = invoke(invocation);
			invocationSuccess = true;
		} catch (Exception ex) {
			invocationSuccess = false;
		}
		if (invocationSuccess) {
			if (!CollectionUtils.isEmpty(cacheToInvocationsMap.get(cacheName))) {
				// 通过cacheManager获取操作缓存的cache对象
				Cache cache = cacheManager.getCache(cacheName);
				// 通过Cache对象更新缓存
				cache.put(invocation.getKey(), computed);
			}
		}
	}

	private Object invoke(CachedInvocation invocation)
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

		final MethodInvoker invoker = new MethodInvoker();
		invoker.setTargetObject(invocation.getTargetBean());
		invoker.setArguments(invocation.getArguments());
		invoker.setTargetMethod(invocation.getTargetMethod().getName());
		invoker.prepare();

		return invoker.invoke();
	}

	@Override
	public void registerInvocation(Object targetBean, Method targetMethod, Object[] arguments,
			Set<String> annotatedCacheNames, String cacheKey) {

		// 获取注解上真实的value值
		Collection<String> cacheNames = generateValue(annotatedCacheNames);

		// 获取注解上的key属性值
		Class<?> targetClass = getTargetClass(targetBean);
		Collection<? extends Cache> caches = getCache(cacheNames);
		Object key = generateKey(caches, cacheKey, targetMethod, arguments, targetBean, targetClass,
				CacheOperationExpressionEvaluator.NO_RESULT);

		// 新建一个代理对象（记录了缓存注解的方法类信息）
		final CachedInvocation invocation = new CachedInvocation(key, targetBean, targetMethod, arguments);
		for (final String cacheName : cacheNames) {
			if (!cacheToInvocationsMap.containsKey(cacheName)) {
				cacheToInvocationsMap.put(cacheName, new CopyOnWriteArraySet<>());
			}
			cacheToInvocationsMap.get(cacheName).add(invocation);
		}
	}

	@Override
	public void refreshCache(String cacheName) {
		this.refreshCacheByKey(cacheName, null);
	}

	@Override
	public void refreshCacheByKey(String cacheName, String cacheKey) {
		// 如果根据缓存名称没有找到代理信息类的set集合就不执行刷新操作。
		// 只有等缓存有效时间过了，再走到切面哪里然后把代理方法信息注册到这里来。
		if (!CollectionUtils.isEmpty(cacheToInvocationsMap.get(cacheName))) {
			for (final CachedInvocation invocation : cacheToInvocationsMap.get(cacheName)) {
				if (!StringUtils.isBlank(cacheKey) && invocation.getKey().toString().equals(cacheKey)) {
					logger.info("缓存：{}-{}，重新加载数据", cacheName, cacheKey.getBytes());
					refreshCache(invocation, cacheName);
				}
			}
		}
	}

	/**
	 * 获取注解上的value属性值（cacheNames）
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
		return  cacheNames;
	}

    /**
     * 解析SpEL表达式，获取注解上的key属性值
     * 直接扣的Spring解析表达式部分代码
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

}

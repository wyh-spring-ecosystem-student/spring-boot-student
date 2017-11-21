package com.xiaolyuh.redis.cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.MethodInvoker;

import com.xiaolyuh.redis.cache.expression.CacheOperationExpressionEvaluator;

/**
 * 手动刷新缓存实现类
 */
@Component
public class CacheSupportImpl implements CacheSupport, InvocationRegistry {

	private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();

	private KeyGenerator keyGenerator = new SimpleKeyGenerator();

	private final String SEPARATOR = "#";

	/**
	 * 记录容器与所有执行方法信息
	 */
	private Map<String, Set<CachedInvocation>> cacheToInvocationsMap;

	@Autowired
	private CacheManager cacheManager;

	private void refreshCache(CachedInvocation invocation, String cacheName) {

		boolean invocationSuccess;
		Object computed = null;
		try {
			computed = invoke(invocation);
			invocationSuccess = true;
		} catch (Exception ex) {
			invocationSuccess = false;
		}
		if (invocationSuccess) {
			if (cacheToInvocationsMap.get(cacheName) != null) {
				cacheManager.getCache(cacheName).put(invocation.getKey(), computed);
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

	@PostConstruct
	public void initialize() {
		cacheToInvocationsMap = new ConcurrentHashMap<String, Set<CachedInvocation>>(
				cacheManager.getCacheNames().size());
		for (final String cacheName : cacheManager.getCacheNames()) {
			cacheToInvocationsMap.put(cacheName, new CopyOnWriteArraySet<CachedInvocation>());
		}
	}

	@Override
	public void registerInvocation(Object targetBean, Method targetMethod, Object[] arguments,
			Set<String> annotatedCacheNames, String cacheKey) {
		Class<?> targetClass = getTargetClass(targetBean);
		Collection<String> cacheNames = new HashSet<>();
		for (final String cacheName : annotatedCacheNames) {
			String[] cacheParams = cacheName.split(SEPARATOR);
			String realCacheName = cacheParams[0];
			cacheNames.add(realCacheName);
		}
		Collection<? extends Cache> caches = getCache(cacheNames);
		Object key = generateKey(caches, cacheKey, targetMethod, arguments, targetBean, targetClass,
				CacheOperationExpressionEvaluator.NO_RESULT);

		final CachedInvocation invocation = new CachedInvocation(key, targetBean, targetMethod, arguments);
		for (final String cacheName : annotatedCacheNames) {
			String[] cacheParams = cacheName.split(SEPARATOR);
			String realCacheName = cacheParams[0];
			if (!cacheToInvocationsMap.containsKey(realCacheName)) {
				this.initialize();
			}
			cacheToInvocationsMap.get(realCacheName).add(invocation);
		}
	}

	@Override
	public void refreshCache(String cacheName) {
		this.refreshCacheByKey(cacheName, null);
	}

	@Override
	public void refreshCacheByKey(String cacheName, String cacheKey) {
		if (cacheToInvocationsMap.get(cacheName) != null) {
			for (final CachedInvocation invocation : cacheToInvocationsMap.get(cacheName)) {
				if (!StringUtils.isBlank(cacheKey) && invocation.getKey().toString().equals(cacheKey)) {
					refreshCache(invocation, cacheName);
				}
			}
		}
	}

    /**
     * 解析SpEL表达式获取key
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

package com.xiaolyuh.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis分布式锁
 * 
 * @author yuhao.wangwang
 *
 * @version 1.0
 * @date 2017年11月3日 上午10:21:27
 */
public class RedisLock {
	private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

	//////////////////// 静态常量定义开始///////////////////////
	/** 存储到redis中的锁标志 */
	private static final String LOCKED = "LOCKED";

	/** 默认请求锁的超时时间(ms) */
	private static final long TIME_OUT = 40000;

	/** 默认锁的有效时间(s) */
	public static final int EXPIRE = 60;
	//////////////////// 静态常量定义结束///////////////////////

	/** 锁标志对应的key */
	private String key;

	/** 锁的有效时间(s) */
	private int expireTime = EXPIRE;

	/** 请求锁的超时时间(ms) */
	private long timeOut = TIME_OUT;

	/** 锁flag */
	private volatile boolean isLocked = false;
	/**
	 * Redis管理模板
	 */
	private StringRedisTemplate redisTemplate;

	/**
	 * 构造方法
	 * 
	 * @param redisTemplate
	 *            Redis管理模板
	 * @param key
	 *            锁定key
	 * @param expireTime
	 *            锁过期时间 （秒）
	 * @param timeOut
	 *            请求锁超时时间 (毫秒)
	 */
	public RedisLock(StringRedisTemplate redisTemplate, String key, int expireTime, long timeOut) {
		this.key = key;
		this.expireTime = expireTime;
		this.timeOut = timeOut;
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 构造方法
	 * 
	 * @param redisTemplate
	 *            Redis管理模板
	 * @param key
	 *            锁定key
	 * @param expireTime
	 *            锁过期时间
	 */
	public RedisLock(StringRedisTemplate redisTemplate, String key, int expireTime) {
		this.key = key;
		this.expireTime = expireTime;
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 构造方法(默认请求锁超时时间30秒，锁过期时间60秒)
	 * 
	 * @param redisTemplate
	 *            Redis管理模板
	 * @param key
	 *            锁定key
	 */
	public RedisLock(StringRedisTemplate redisTemplate, String key) {
		this.key = key;
		this.redisTemplate = redisTemplate;
	}

	public boolean lock() {
		// 系统当前时间，纳秒
		long nowTime = System.nanoTime();
		// 请求锁超时时间，纳秒
		long timeout = timeOut * 1000000;
		final Random random = new Random();

		// 不断循环向Master节点请求锁，当请求时间(System.nanoTime() - nano)超过设定的超时时间则放弃请求锁
		// 这个可以防止一个客户端在某个宕掉的master节点上阻塞过长时间
		// 如果一个master节点不可用了，应该尽快尝试下一个master节点
		while ((System.nanoTime() - nowTime) < timeout) {
			// 将锁作为key存储到redis缓存中，存储成功则获得锁
			if (redisTemplate.opsForValue().setIfAbsent(key, LOCKED)) {
				isLocked = true;
				// 设置锁的有效期，也是锁的自动释放时间，也是一个客户端在其他客户端能抢占锁之前可以执行任务的时间
				// 可以防止因异常情况无法释放锁而造成死锁情况的发生
				redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);

				// 上锁成功结束请求
				break;
			}
			// 获取锁失败时，应该在随机延时后进行重试，避免不同客户端同时重试导致谁都无法拿到锁的情况出现
			// 睡眠3毫秒后继续请求锁
			try {
				Thread.sleep(3, random.nextInt(50000));
			} catch (InterruptedException e) {
				logger.error("获取分布式锁休眠被中断：", e);
			}
		}
		return isLocked;

	}

	public boolean isLock() {
		redisTemplate.getConnectionFactory().getConnection().time();
		return redisTemplate.hasKey(key);
	}

	public void unlock() {
		// 释放锁
		// 不管请求锁是否成功，只要已经上锁，客户端都会进行释放锁的操作
		if (isLocked) {
			redisTemplate.delete(key);
		}
	}
}

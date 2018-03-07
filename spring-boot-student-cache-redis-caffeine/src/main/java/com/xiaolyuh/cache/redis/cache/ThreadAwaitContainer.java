package com.xiaolyuh.cache.redis.cache;

import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 等待线程容器
 * @author yuhao.wang
 */
public class ThreadAwaitContainer {
    private final Map<String, Set<Thread>> threadMap = new ConcurrentHashMap<>();

    /**
     * 线程等待,最大等待100毫秒
     * @param key
     * @throws InterruptedException
     */
    public final void await(String key) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Set<Thread> threadSet = threadMap.get(key);
        if (threadSet == null) {
            threadSet = new HashSet<>();
            threadMap.put(key, threadSet);
        }
        threadSet.add(Thread.currentThread());
        // 阻塞一定的时间
        LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(100));
    }

    /**
     * 线程唤醒
     * @param key
     */
    public final void signalAll(String key) {
        Set<Thread> threadSet = threadMap.get(key);
        if (!CollectionUtils.isEmpty(threadSet)) {
            for (Thread thread : threadSet) {
                LockSupport.unpark(thread);
            }
        }
    }

}

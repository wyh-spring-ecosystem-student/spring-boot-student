package com.xiaolyuh.redis.cache;

/**
 * @author yuhao.wang
 */
public class CacheTime {

    public CacheTime(long expirationSecondTime, long preloadSecondTime) {
        this.expirationSecondTime = expirationSecondTime;
        this.preloadSecondTime = preloadSecondTime;
    }

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     * 单位：秒
     */
    private long preloadSecondTime = 0;

    /**
     * 缓存有效时间
     */
    private long expirationSecondTime;

    public long getPreloadSecondTime() {
        return preloadSecondTime;
    }

    public long getExpirationSecondTime() {
        return expirationSecondTime;
    }
}

package com.xiaolyuh.cache.redis.cache;

/**
 * @author yuhao.wang
 */
public class SecondaryCacheSetting {

    /**
     * @param expirationSecondTime 设置redis缓存的有效时间，单位秒
     * @param preloadSecondTime    设置redis缓存的自动刷新时间，单位秒
     */
    public SecondaryCacheSetting(long expirationSecondTime, long preloadSecondTime) {
        this.expirationSecondTime = expirationSecondTime;
        this.preloadSecondTime = preloadSecondTime;
    }

    /**
     * @param usedFirstCache       是否启用一级缓存，默认true
     * @param expirationSecondTime 设置redis缓存的有效时间，单位秒
     * @param preloadSecondTime    设置redis缓存的自动刷新时间，单位秒
     */
    public SecondaryCacheSetting(boolean usedFirstCache, long expirationSecondTime, long preloadSecondTime) {
        this.expirationSecondTime = expirationSecondTime;
        this.preloadSecondTime = preloadSecondTime;
        this.usedFirstCache = usedFirstCache;
    }

    /**
     * @param expirationSecondTime 设置redis缓存的有效时间，单位秒
     * @param preloadSecondTime    设置redis缓存的自动刷新时间，单位秒
     * @param forceRefresh         是否使用强制刷新（走数据库），默认false
     */
    public SecondaryCacheSetting(long expirationSecondTime, long preloadSecondTime, boolean forceRefresh) {
        this.expirationSecondTime = expirationSecondTime;
        this.preloadSecondTime = preloadSecondTime;
        this.forceRefresh = forceRefresh;
    }

    /**
     * @param expirationSecondTime 设置redis缓存的有效时间，单位秒
     * @param preloadSecondTime    设置redis缓存的自动刷新时间，单位秒
     * @param usedFirstCache       是否启用一级缓存，默认true
     * @param forceRefresh         是否使用强制刷新（走数据库），默认false
     */
    public SecondaryCacheSetting(long expirationSecondTime, long preloadSecondTime, boolean usedFirstCache, boolean forceRefresh) {
        this.expirationSecondTime = expirationSecondTime;
        this.preloadSecondTime = preloadSecondTime;
        this.usedFirstCache = usedFirstCache;
        this.forceRefresh = forceRefresh;
    }

    /**
     * 缓存有效时间
     */
    private long expirationSecondTime;

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     * 单位：秒
     */
    private long preloadSecondTime = 0;

    /**
     * 是否使用二级缓存，默认是true
     */
    private boolean usedFirstCache = true;

    /**
     * 是否使用强刷新（走数据库），默认是false
     */
    private boolean forceRefresh = false;

    public long getPreloadSecondTime() {
        return preloadSecondTime;
    }

    public long getExpirationSecondTime() {
        return expirationSecondTime;
    }

    public boolean getUsedFirstCache() {
        return usedFirstCache;
    }

    public boolean getForceRefresh() {
        return forceRefresh;
    }
}

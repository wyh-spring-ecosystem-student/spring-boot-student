package com.xiaolyuh.redis.cache;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 标记了缓存注解的方法类信息
 * 用于主动刷新缓存时调用原始方法加载数据
 *
 * @author yuhao.wang
 */
public final class CachedInvocation implements Serializable{

    private Object key;
    private String targetBean;
    private String targetMethod;
    private List<Object> arguments;
    private List<String> parameterTypes = new ArrayList<>();

    public CachedInvocation() {
    }

    public CachedInvocation(Object key, Object targetBean, String targetMethod, Class[] parameterTypes, Object[] arguments) {
        this.key = key;
        this.targetBean = targetBean.getClass().getName();
        this.targetMethod = targetMethod;
        if (arguments != null && arguments.length != 0) {
            this.arguments = Arrays.asList(arguments);
        }
        if (parameterTypes != null && parameterTypes.length != 0) {
            for (Class clazz: parameterTypes) {
                this.parameterTypes.add(clazz.getName());
            }
        }
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getTargetBean() {
        return targetBean;
    }

    public void setTargetBean(String targetBean) {
        this.targetBean = targetBean;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }

    public List<String> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<String> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * 必须重写equals和hashCode方法，否则放到set集合里没法去重
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CachedInvocation that = (CachedInvocation) o;

        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}


package com.xiaolyuh.init.destory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 测试Bean的三种初始化、销毁方式和执行顺序
 *
 * @author yuhao.wang3
 */
public class InitBeanAndDestroyBean implements InitializingBean, DisposableBean {
    public String say() {
        return "Hello!" + this.getClass().getName();
    }

    public InitBeanAndDestroyBean() {
        System.out.println("执行InitBeanAndDestroyBean构造方法");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("接口-执行InitBeanAndDestroyBeanTest：destroy方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("接口-执行InitBeanAndDestroyBeanTest：afterPropertiesSet方法");
    }

    @PostConstruct
    public void postConstructstroy() {
        System.out.println("注解-执行InitBeanAndDestroyBeanTest：preDestroy方法");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("注解--执行InitBeanAndDestroyBeanTest：preDestroy方法");
    }

    public void initMethod() {
        System.out.println("XML配置-执行InitBeanAndDestroyBeanTest：init-method方法");
    }

    public void destroyMethod() {
        System.out.println("XML配置-执行InitBeanAndDestroyBeanTest：destroy-method方法");
    }
}
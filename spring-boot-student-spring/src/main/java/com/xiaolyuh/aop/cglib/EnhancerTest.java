package com.xiaolyuh.aop.cglib;

import org.junit.Test;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;

public class EnhancerTest {

    @Test
    public void contextTest() {
        // 输出cglib的代理的class到磁盘。
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\class");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(EnhancerDemo.class);
        enhancer.setCallback(new MethodInterceptorImpl());

        EnhancerDemo proxy = (EnhancerDemo) enhancer.create();

        proxy.test();
        System.out.println(proxy);
    }
}
package com.xiaolyuh.aop.jdk;

import com.xiaolyuh.aop.JdkProxySourceClassUtil;
import org.junit.Test;

public class ProxyTest {

    @Test
    public void contextTest() {
        // 实例化目标对象
        UserService userService = new UserServiceImpl();
        // 实例化 InvocationHandler
        MyInvocationHandler invocationHandler = new MyInvocationHandler(userService);
        // 生成代理对象
        UserService proxy = (UserService) invocationHandler.getProxy();
        // 输出代理类的class
        JdkProxySourceClassUtil.writeClassToDisk(proxy.getClass().getSimpleName(), UserService.class);
        // 调用代理对象的方法
        proxy.add();
    }
}
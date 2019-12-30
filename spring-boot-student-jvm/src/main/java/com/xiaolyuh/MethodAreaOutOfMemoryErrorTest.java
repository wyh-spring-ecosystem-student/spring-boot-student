package com.xiaolyuh;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * java 方法区的内存溢出
 * <p>
 * VM Args JDK 1.6: set JAVA_OPTS=-verbose:gc -XX:PermSize10 -XX:MaxPermSize10m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\dump
 *
 * @author yuhao.wang3
 * @since 2019/11/30 17:09
 */
public class MethodAreaOutOfMemoryErrorTest {

    class MethodAreaOOM {
    }

    public static void main(String[] args) {
        MethodAreaOutOfMemoryErrorTest h = new MethodAreaOutOfMemoryErrorTest();
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(MethodAreaOOM.class);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invokeSuper(o, args);
                }
            });
            enhancer.create();
        }
    }
}



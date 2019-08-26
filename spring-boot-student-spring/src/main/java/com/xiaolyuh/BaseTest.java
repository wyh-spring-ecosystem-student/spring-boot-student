package com.xiaolyuh;

import org.junit.After;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 11:11
 */
public class BaseTest {

    /**
     * 使用容器
     */
    protected AnnotationConfigApplicationContext context = null;

    public void before(Class aClass) {
        context = new AnnotationConfigApplicationContext(aClass);
        System.out.println();
        System.out.println();
        System.out.println("IOC容器完成初始化---->");
        System.out.println();
        System.out.println();
    }

    @After
    public void closeContext() {
        context.close();
        System.out.println();
        System.out.println();
        System.out.println("关闭IOC容器---->");
        System.out.println();
        System.out.println();
    }
}

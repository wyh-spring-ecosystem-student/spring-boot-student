package com.xiaolyuh.lazy;

import com.xiaolyuh.PrintSpringBeanUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 11:11
 */
public class LazyTest {

    /**
     * 使用容器
     */
    AnnotationConfigApplicationContext context = null;

    @Before
    public void before() {
        context = new AnnotationConfigApplicationContext(LazyConfig.class);
        System.out.println();
        System.out.println();
        System.out.println("IOC容器完成初始化---->");
        System.out.println();
        System.out.println();
    }

    @Test
    public void contextTest() {

        PrintSpringBeanUtil.printlnBean(context);
        System.out.println("开始获取容器中的Bean");
        LazyTestBean bean1 = context.getBean(LazyTestBean.class);
        LazyTestBean bean2 = context.getBean(LazyTestBean.class);
        System.out.println(bean1 == bean2);
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

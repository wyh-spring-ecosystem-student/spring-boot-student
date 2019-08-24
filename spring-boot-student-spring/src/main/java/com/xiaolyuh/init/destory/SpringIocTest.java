package com.xiaolyuh.init.destory;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 测试Bean的三种初始化、销毁方式和执行顺序
 *
 * @author yuhao.wang3
 */
public class SpringIocTest {
    /**
     * 使用容器
     */
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);

    @Test
    public void contextTest() {
        // 测试Bean的三种初始化、销毁方式和执行顺序
        System.out.println(context.getBean(InitBeanAndDestroyBean.class));
    }

    @After
    public void closeContext() {
        context.close();
    }

}
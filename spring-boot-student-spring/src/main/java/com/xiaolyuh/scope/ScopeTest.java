package com.xiaolyuh.scope;

import com.xiaolyuh.PrintSpringBeanUtil;
import org.junit.After;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 11:11
 */
public class ScopeTest {

    /**
     * 使用容器
     */
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScopeConfig.class);

    @Test
    public void contextTest() {

        PrintSpringBeanUtil.printlnBean(context);
        System.out.println("开始比较容器中的Bean");
        ScopeTestBean bean1 = context.getBean(ScopeTestBean.class);
        ScopeTestBean bean2 = context.getBean(ScopeTestBean.class);
        System.out.println(bean1 == bean2);

        PrintSpringBeanUtil.printlnBean(context);
    }

    @After
    public void closeContext() {
        context.close();
    }
}

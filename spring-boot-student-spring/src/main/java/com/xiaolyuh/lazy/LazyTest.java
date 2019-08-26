package com.xiaolyuh.lazy;

import com.xiaolyuh.BaseTest;
import com.xiaolyuh.PrintSpringBeanUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 11:11
 */
public class LazyTest extends BaseTest {

    @Before
    public void before() {
        super.before(ImportConfig.class);
    }

    @Test
    public void contextTest() {

        PrintSpringBeanUtil.printlnBean(context);
        System.out.println("开始获取容器中的Bean");
        LazyTestBean bean1 = context.getBean(LazyTestBean.class);
        LazyTestBean bean2 = context.getBean(LazyTestBean.class);
        System.out.println(bean1 == bean2);
    }
}

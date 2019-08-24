package com.xiaolyuh.component.scan;

import com.xiaolyuh.PrintSpringBeanUtil;
import org.junit.After;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 11:11
 */
public class ComponentScanTest {

    /**
     * 使用容器
     */
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ComponentScanConfig.class);

    @Test
    public void contextTest() {
        PrintSpringBeanUtil.printlnBean(context);
    }

    @After
    public void closeContext() {
        context.close();
    }
}

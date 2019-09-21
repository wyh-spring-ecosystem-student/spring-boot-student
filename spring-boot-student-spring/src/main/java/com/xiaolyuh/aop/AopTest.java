package com.xiaolyuh.aop;

import com.xiaolyuh.BaseTest;
import com.xiaolyuh.PrintSpringBeanUtil;
import com.xiaolyuh.aop.controller.UserController;
import org.junit.Before;
import org.junit.Test;

public class AopTest extends BaseTest {

    @Before
    public void before() {
        super.before(AopConfig.class);
    }

    @Test
    public void contextTest() {

        PrintSpringBeanUtil.printlnBean(context);
        System.out.println("开始比较容器中的Bean");
        UserController bean = context.getBean(UserController.class);

        bean.div(4, 2);
    }
}
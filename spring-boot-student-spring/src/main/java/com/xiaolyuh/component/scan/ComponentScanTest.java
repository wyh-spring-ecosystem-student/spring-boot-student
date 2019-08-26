package com.xiaolyuh.component.scan;

import com.xiaolyuh.BaseTest;
import com.xiaolyuh.PrintSpringBeanUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 11:11
 */
public class ComponentScanTest extends BaseTest {

    @Before
    public void before() {
        super.before(ComponentScanConfig.class);
    }

    @Test
    public void contextTest() {
        PrintSpringBeanUtil.printlnBean(context);
    }
}

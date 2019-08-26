package com.xiaolyuh.iimport;

import com.xiaolyuh.BaseTest;
import com.xiaolyuh.PrintSpringBeanUtil;
import com.xiaolyuh.iimport.bean.MonkeyTestBean;
import org.junit.Before;
import org.junit.Test;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 11:11
 */
public class ImportTest extends BaseTest {

    @Before
    public void before() {
        super.before(ImportConfig.class);
    }

    @Test
    public void contextTest() {

        PrintSpringBeanUtil.printlnBean(context);
        System.out.println("开始获取容器中的Bean");
        AnimalFactoryBean animalFactoryBean = context.getBean(AnimalFactoryBean.class);
        System.out.println(animalFactoryBean.getObjectType());
        MonkeyTestBean monkeyTestBean = (MonkeyTestBean) context.getBean("monkeyTestBean");
        MonkeyTestBean monkeyTestBean2 = context.getBean(MonkeyTestBean.class);
        System.out.println(monkeyTestBean == monkeyTestBean2);
        monkeyTestBean.hobby();

    }

}

package com.xiaolyuh.iimport;

import com.xiaolyuh.BaseTest;
import com.xiaolyuh.PrintSpringBeanUtil;
import com.xiaolyuh.iimport.bean.MonkeyTestBean;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;

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
        System.out.println(animalFactoryBean);
        // 加了一个&特殊符号就会返回一个工厂Bean
        AnimalFactoryBean factoryBean = (AnimalFactoryBean) context.getBean("&monkeyTestBean");
        System.out.println(factoryBean);

        MonkeyTestBean monkeyTestBean = (MonkeyTestBean) context.getBean("monkeyTestBean");
        MonkeyTestBean monkeyTestBean2 = context.getBean(MonkeyTestBean.class);
        System.out.println("比较两个对象是否一样:" + (monkeyTestBean == monkeyTestBean2));

        BeanDefinition beanDefinition = context.getBeanDefinition("monkeyTestBean");
        monkeyTestBean.hobby();

        System.out.println("是否单例 :" + beanDefinition.isSingleton());

    }

}

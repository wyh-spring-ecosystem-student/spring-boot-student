package com.xiaolyuh.iimport;

import com.xiaolyuh.iimport.bean.MonkeyTestBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author yuhao.wang3
 * @since 2019/8/26 10:58
 */
public class AnimalFactoryBean implements FactoryBean {

    public AnimalFactoryBean() {
        System.out.println();
        System.out.println(getClass().getSimpleName() + " 初始化");
        System.out.println();
    }

    /**
     * 获取实例
     *
     * @return
     * @throws Exception
     */
    @Override
    public MonkeyTestBean getObject() throws Exception {

        return new MonkeyTestBean();
    }

    /**
     * 获取示例类型
     *
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return MonkeyTestBean.class;
    }

    /**
     * 是否单例
     *
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}

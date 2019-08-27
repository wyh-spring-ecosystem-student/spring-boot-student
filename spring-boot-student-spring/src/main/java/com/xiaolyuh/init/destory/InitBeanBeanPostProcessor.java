package com.xiaolyuh.init.destory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 初始化Bean的后置处理器
 *
 * @author yuhao.wang3
 * @since 2019/8/26 20:44
 */
@Component
public class InitBeanBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("后置处理器-执行：postProcessBeforeInitialization 方法 处理：" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("后置处理器-执行 执行：postProcessAfterInitialization 方法 处理：" + beanName);
        return bean;
    }
}

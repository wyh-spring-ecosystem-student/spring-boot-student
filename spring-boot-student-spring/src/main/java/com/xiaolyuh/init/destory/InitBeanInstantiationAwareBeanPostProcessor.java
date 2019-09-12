package com.xiaolyuh.init.destory;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;

/**
 * Bean实例化的后置处理器
 *
 * @author yuhao.wang3
 * @since 2019/9/5 19:43
 */
@Component
public class InitBeanInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("Bean实例化的后置处理器-执行：InitBeanInstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation 方法 处理：" + beanName);
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("Bean实例化的后置处理器-执行：InitBeanInstantiationAwareBeanPostProcessor.postProcessAfterInstantiation 方法 处理：" + beanName);
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        System.out.println("Bean实例化的后置处理器-执行：InitBeanInstantiationAwareBeanPostProcessor.postProcessPropertyValues 方法 处理：" + beanName);
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Bean实例化的后置处理器-执行：InitBeanInstantiationAwareBeanPostProcessor.postProcessBeforeInitialization 方法 处理：" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Bean实例化的后置处理器-执行：InitBeanInstantiationAwareBeanPostProcessor.postProcessAfterInitialization 方法 处理：" + beanName);
        return bean;
    }
}

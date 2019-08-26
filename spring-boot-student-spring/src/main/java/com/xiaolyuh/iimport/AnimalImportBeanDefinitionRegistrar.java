package com.xiaolyuh.iimport;

import com.xiaolyuh.iimport.bean.CatTestBean;
import com.xiaolyuh.iimport.bean.DogTestBean;
import com.xiaolyuh.iimport.bean.PigTestBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 只有动物园里面有 猫和狗的时候我么才将猪注入进去.
 * 通过该方式注册Bean，必须将Bean封装成 RootBeanDefinition。
 * ImportBeanDefinitionRegistrar注册器，在注册bean的过程中会在最后执行。
 *
 * @author yuhao.wang3
 * @since 2019/8/26 10:58
 */
public class AnimalImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * @param importingClassMetadata 当前类的注解信息
     * @param registry               注册器，通过注册器将特定类注册到容器中
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 猫和狗的Bean我们可以声明一个注解，类似Spring Boot的条件注解
        boolean isContainsDog = registry.containsBeanDefinition(DogTestBean.class.getName());
        boolean isContainsCat = registry.containsBeanDefinition(CatTestBean.class.getName());

        if (isContainsDog && isContainsCat) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition(PigTestBean.class);
            // 第一个参数是Bean id ，第二个是RootBeanDefinition
            registry.registerBeanDefinition("pigTestBean", beanDefinition);
        }
    }
}

package com.xiaolyuh;

import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * 打印 Spring 容器中的 Bean
 *
 * @author yuhao.wang3
 * @since 2019/8/24 16:05
 */
public class PrintSpringBeanUtil {
    public static void printlnBean(ApplicationContext context) {
        System.out.println();
        System.out.println();
        System.out.println("打印 Spring 容器中的Bean  开始");
        String[] names = context.getBeanDefinitionNames();
        Arrays.stream(names).forEach(System.out::println);
        System.out.println("打印 Spring 容器中的Bean  结束");
        System.out.println();
        System.out.println();
    }
}

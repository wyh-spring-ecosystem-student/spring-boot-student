package com.xiaolyuh.init.destory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 测试Bean的三种初始化、销毁方式和执行顺序
 *
 * @author yuhao.wang3
 */
// 声明成配置文件
@Configuration
// 扫描包
@ComponentScan("com.xiaolyuh.init.destory")
public class MyConfig {

    // 测试Bean的几种初始化和销毁方式，和执行顺序
    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
//    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public InitBeanAndDestroyBean initBeanAndDestroyBean() {
        return new InitBeanAndDestroyBean();
    }
}
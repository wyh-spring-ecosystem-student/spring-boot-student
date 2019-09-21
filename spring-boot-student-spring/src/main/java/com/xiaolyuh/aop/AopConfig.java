package com.xiaolyuh.aop;

import com.xiaolyuh.aop.aspect.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author yuhao.wang3
 * @since 2019/9/12 13:52
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan({"com.xiaolyuh.aop"})
public class AopConfig {

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}

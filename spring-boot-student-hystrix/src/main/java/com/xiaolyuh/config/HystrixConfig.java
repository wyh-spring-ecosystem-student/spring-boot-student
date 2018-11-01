package com.xiaolyuh.config;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.xiaolyuh.hystrix.MdcHystrixConcurrencyStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class HystrixConfig {

    //用来拦截处理HystrixCommand注解
    @Bean
    public HystrixCommandAspect hystrixAspect() {
        return new HystrixCommandAspect();
    }

    @PostConstruct
    public void init() {
        HystrixPlugins.getInstance().registerConcurrencyStrategy(new MdcHystrixConcurrencyStrategy());
    }

}
package com.xiaolyuh.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

    // 注册插件方式1
    @Bean
    public EditPlugin myPlugin() {
        return new EditPlugin();
    }
//
//
//    // 注册插件方式2
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return new ConfigurationCustomizer() {
//            @Override
//            public void customize(org.apache.ibatis.session.Configuration configuration) {
//                //插件拦截链采用了责任链模式，执行顺序和加入连接链的顺序有关
//                EditPlugin myPlugin = new EditPlugin();
//                configuration.addInterceptor(myPlugin);
//            }
//        };
//    }

//    // 注册插件方式2
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> {
//            //插件拦截链采用了责任链模式，执行顺序和加入连接链的顺序有关
//            EditPlugin myPlugin = new EditPlugin();
//            configuration.addInterceptor(myPlugin);
//        };
//    }
}

package com.xiaolyuh.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 15:29
 */
@Configuration
public class ScopeConfig {

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean("scopeTestBean")
    public ScopeTestBean scopeTestBean() {
        return new ScopeTestBean();
    }
}

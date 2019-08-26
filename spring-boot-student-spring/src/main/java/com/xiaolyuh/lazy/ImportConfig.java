package com.xiaolyuh.lazy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 15:58
 */
@Configuration
public class ImportConfig {

    @Lazy
    @Bean
    public LazyTestBean lazyTestBean() {
        return new LazyTestBean();
    }
}

package com.xiaolyuh.core;

import com.xiaolyuh.aspect.LogAspect;
import com.xiaolyuh.aspect.LogTrackAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author yuhao.wang3
 * @since 2019/9/10 10:32
 */
public class TrackConfig extends WebMvcConfigurerAdapter {
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @Bean
    public LogTrackAspect logTrackAspect() {
        return new LogTrackAspect();
    }

//    @PostConstruct
//    public void registerHystrixConcurrencyStrategy() {
//        HystrixPlugins.getInstance().registerConcurrencyStrategy(new MdcHystrixConcurrencyStrategy());
//    }
}

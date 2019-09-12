package com.xiaolyuh.di;

import com.wlqq.etc.common.log.annotations.EnableAspectJLog;
import com.xiaolyuh.di.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuhao.wang3
 * @since 2019/8/30 15:07
 */
@Configuration
@ComponentScan({"com.xiaolyuh.di"})
@EnableAspectJLog
public class DiConfig {

    @Bean
    public UserService userService2() {
        UserService userService = new UserService();
        userService.setId(2);
        return userService;
    }

}

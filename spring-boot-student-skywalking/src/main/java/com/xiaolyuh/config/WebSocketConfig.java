package com.xiaolyuh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
/**
 * @ Auther: 马超伟
 * @ Date: 2020/06/16/14:35
 * @ Description: 开启WebSocket支持
 * @author admin
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
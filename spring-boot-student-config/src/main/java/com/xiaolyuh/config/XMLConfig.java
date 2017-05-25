package com.xiaolyuh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:config/some-context.xml"})
public class XMLConfig {

}

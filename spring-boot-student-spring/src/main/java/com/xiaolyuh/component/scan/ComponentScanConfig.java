package com.xiaolyuh.component.scan;

import com.xiaolyuh.component.scan.controller.TestController;
import com.xiaolyuh.component.scan.service.TestService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * 包扫描测试类
 *
 * @author yuhao.wang3
 * @since 2019/8/24 10:39
 */
@Configuration
@ComponentScan(value = "com.xiaolyuh.component.scan", includeFilters = {
        // 只扫描Controller注解的 Bean
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class}),
        // 只扫描特定的类
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {TestService.class}),
        // 使用特定的过滤规则
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {CustomTypeFilter.class})
}, useDefaultFilters = false)
public class ComponentScanConfig {

}

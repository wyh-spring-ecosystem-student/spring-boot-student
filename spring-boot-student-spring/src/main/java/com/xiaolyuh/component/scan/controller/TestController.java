package com.xiaolyuh.component.scan.controller;

import org.springframework.stereotype.Controller;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 11:19
 */
@Controller
public class TestController {
    public TestController() {
        System.out.println();
        System.out.println(getClass().getSimpleName() + " 初始化");
        System.out.println();
    }
}

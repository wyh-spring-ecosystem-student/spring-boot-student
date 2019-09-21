package com.xiaolyuh.aop.controller;

import com.xiaolyuh.aop.annotations.Log;
import org.springframework.stereotype.Controller;

/**
 * @author yuhao.wang3
 * @since 2019/9/12 15:50
 */
@Controller
public class UserController {

    // 连接点
    @Log("除法接口")
    public Object div(int i, int j) {
        System.out.println("业务接口执行-------------------");
        return i / j;
    }

    public Object add(int i, int j) {
        return i + j;
    }
}

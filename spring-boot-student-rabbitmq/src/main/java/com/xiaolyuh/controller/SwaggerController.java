package com.xiaolyuh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author yuhao.wang
 */
@Controller
public class SwaggerController {

    @GetMapping("/")
    public String swaggerUi() {

        return "redirect:/swagger-ui.html";
    }
}
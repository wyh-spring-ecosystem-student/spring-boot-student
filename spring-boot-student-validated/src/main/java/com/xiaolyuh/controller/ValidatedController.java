package com.xiaolyuh.controller;

import com.xiaolyuh.param.InputParam;
import com.xiaolyuh.service.ValidatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhao.wang
 */
@RestController
public class ValidatedController {

    @Autowired
    private ValidatedService validatedService;

    @PostMapping("validated")
    public String validatedTest(@Validated(InputParam.ParameterGroup1.class) InputParam inputParam) {

        return validatedService.validatedTest(inputParam);
    }
}
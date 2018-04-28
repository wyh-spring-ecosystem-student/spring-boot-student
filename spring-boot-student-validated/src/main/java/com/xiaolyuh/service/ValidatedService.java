package com.xiaolyuh.service;

import com.xiaolyuh.param.InputParam;
import org.springframework.stereotype.Service;

/**
 * @author yuhao.wang
 */
@Service
public class ValidatedService {

    public String validatedTest(InputParam inputParam) {

        return "Hello World";
    }
}
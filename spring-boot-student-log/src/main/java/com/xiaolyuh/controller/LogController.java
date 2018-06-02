package com.xiaolyuh.controller;

import com.xiaolyuh.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhao.wang
 */
@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping(value = "log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object sendMsg() {
        logService.log();
        return "发送成";
    }

}
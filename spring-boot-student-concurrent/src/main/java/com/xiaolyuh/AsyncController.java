package com.xiaolyuh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncController {
    @Autowired
    private MainThreadService mainThreadService;

    @PostMapping("/async")
    public String helloWorld() throws Exception {
        mainThreadService.asyncMethod();
        return "Hello World";
    }
}

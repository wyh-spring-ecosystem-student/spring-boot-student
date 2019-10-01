package com.xiaolyuh.di.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 循环依赖测试
 *
 * @author yuhao.wang3
 * @since 2019/9/28 11:04
 */
@Service
public class AService {
    @Autowired
    private BService bService;

//    @Autowired
//    public AService(BService bService) {
//        this.bService = bService;
//    }

    @Autowired
    public void setbService(BService bService) {
        this.bService = bService;
    }
}

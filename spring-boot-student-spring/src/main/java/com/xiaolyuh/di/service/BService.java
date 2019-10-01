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
public class BService {
    @Autowired
    private AService aService;

//    @Autowired
//    public BService(AService aService) {
//        this.aService = aService;
//    }


    @Autowired
    public void setaService(AService aService) {
        this.aService = aService;
    }
}

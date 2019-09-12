package com.xiaolyuh.di.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @author yuhao.wang3
 * @since 2019/8/31 13:28
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private Integer id = 1;

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserService{" +
                "id=" + id +
                '}';
    }
}

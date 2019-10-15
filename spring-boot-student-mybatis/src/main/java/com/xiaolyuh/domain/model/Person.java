package com.xiaolyuh.domain.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 名称
     */
    private String userName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 地址
     */
    private String address;
}


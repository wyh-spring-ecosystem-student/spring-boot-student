package com.xiaolyuh.mq.message;

import java.io.Serializable;

/**
 * 发放消息的消息体
 *
 * @author yuhao.wang
 */
public class SendMessage implements Serializable {
    private static final long serialVersionUID = -4731326195678504565L;

    /**
     * ID
     */
    private long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private int age;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

package com.xiaolyuh.iimport.bean;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 15:29
 */
public class MonkeyTestBean {

    public MonkeyTestBean() {
        System.out.println();
        System.out.println(getClass().getSimpleName() + " 初始化");
        System.out.println();
    }

    public void hobby() {
        System.out.println("我爱吃香蕉");
    }
}

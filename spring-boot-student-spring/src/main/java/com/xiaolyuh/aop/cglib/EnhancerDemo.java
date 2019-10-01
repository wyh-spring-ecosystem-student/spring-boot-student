package com.xiaolyuh.aop.cglib;

/**
 * @author yuhao.wang3
 * @since 2019/9/18 17:51
 */
public final class EnhancerDemo {

    public String test() {
        System.out.println("-------------业务逻辑方法 test ------------");
        return "我是返回值";
    }

    public final String test2() {
        System.out.println("-------------业务逻辑方法 test2 ------------");
        return "我是返回值2";
    }
}

package com.xiaolyuh.lazy;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 15:29
 */
public class LazyTestBean {

    public LazyTestBean() {
        System.out.println();
        System.out.println(getClass().getSimpleName() + " 初始化");
        System.out.println();
    }
}

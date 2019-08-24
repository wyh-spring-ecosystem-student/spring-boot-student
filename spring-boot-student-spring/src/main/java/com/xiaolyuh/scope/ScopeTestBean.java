package com.xiaolyuh.scope;

/**
 * @author yuhao.wang3
 * @since 2019/8/24 15:29
 */
public class ScopeTestBean {

    public ScopeTestBean() {
        System.out.println();
        System.out.println(getClass().getSimpleName() + " 初始化");
        System.out.println();
    }
}

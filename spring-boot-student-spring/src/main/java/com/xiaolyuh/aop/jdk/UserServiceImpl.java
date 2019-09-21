package com.xiaolyuh.aop.jdk;

/**
 * @author yuhao.wang3
 * @since 2019/9/18 17:29
 */
public class UserServiceImpl implements UserService {
    @Override
    public void add() {
        System.out.println("-------------业务逻辑方法 add ------------");
    }
}

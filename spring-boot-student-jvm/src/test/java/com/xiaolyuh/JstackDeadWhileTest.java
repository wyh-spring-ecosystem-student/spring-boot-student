package com.xiaolyuh;

/**
 * jstack 死循环
 *
 * @author yuhao.wang3
 * @since 2020/01/15 17:09
 */
public class JstackDeadWhileTest {

    public static void main(String[] args) {
        int i = 1;
        while (++i > 0) {
            System.out.println(i);
        }
    }
}

package com.xiaolyuh;

/**
 * java 虚拟机栈和本地方法栈内存溢出测试
 * <p>
 * 单个线程请求的栈深度大于虚拟机所允许的最大深度
 * <p>
 * VM Args: -Xss128k
 *
 * @author yuhao.wang3
 * @since 2019/11/30 17:09
 */
public class StackOverflowErrorErrorTest {
    private int stackLength = 0;

    public void stackLeak(String ags1, String ags2, String ags3) {
        stackLength++;
        stackLeak(ags1, ags2, ags3);
    }

    public static void main(String[] args) {
        StackOverflowErrorErrorTest sof = new StackOverflowErrorErrorTest();
        try {
            sof.stackLeak("ags1", "ags2", "ags3");
        } catch (Throwable e) {
            System.out.println("stackLength::" + sof.stackLength);
            e.printStackTrace();
        }
    }
}

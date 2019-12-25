package com.xiaolyuh;

/**
 * 测试局部变量表中的Slot重用，对垃圾收集器的影响
 * @author yuhao.wang3
 * @since 2019/11/30 17:09
 */
public class LocalVariableTableSlotGcTest {
    public static void main(String[] args) throws InterruptedException {
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
        }
        // 如果没有i=0 那么即使触发了full gc placeholder变量的空间也不会被回收
        int i = 0;
        // 设置虚拟机运行参数-verbose:gc可以使我们看到垃圾收集器的过程
        System.gc();
    }
}

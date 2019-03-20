package com.xiaolyuh;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author yuhao.wang
 */
public class MainThread {

    public static void main(String[] args) {
        //虚拟机线程管理的接口
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos =
                threadMXBean.dumpAllThreads(false, false);

        for(ThreadInfo threadInfo:threadInfos) {
            System.out.println("["+threadInfo.getThreadId()+"]"+" "
                    +threadInfo.getThreadName());
        }
    }
}
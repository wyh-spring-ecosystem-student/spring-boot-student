package com.xiaolyuh;

import org.assertj.core.util.Lists;

import java.util.List;

/**
 * java 堆内存溢出
 * <p>
 * VM Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\dump
 *
 * @author yuhao.wang3
 * @since 2019/11/30 17:09
 */
public class HeapOutOfMemoryErrorTest {
    public static void main(String[] args) throws InterruptedException {
        List<Object> list = Lists.newArrayList();
        for (long i = 1; i > 0; i++) {
            list.add(new Object());
        }
    }
}

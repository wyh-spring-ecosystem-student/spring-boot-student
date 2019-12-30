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
    static class OOMObject {
        private String field1 = "value1";
        private String field2 = "value2";
    }

    public static void main(String[] args) {
        // 模拟大容器
        List<OOMObject> list = Lists.newArrayList();
        for (long i = 1; i > 0; i++) {
            list.add(new OOMObject());
            if (i % 100_000 == 0) {
                System.out.println(Thread.currentThread().getName() + "::" + i);
            }
        }
    }
}

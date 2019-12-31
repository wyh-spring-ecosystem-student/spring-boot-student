package com.xiaolyuh;

import java.util.ArrayList;
import java.util.List;

/**
 * java 运行时常量池的内存溢出
 * <p>
 * VM Args JDK 1.6: set JAVA_OPTS=-verbose:gc -XX:PermSize=10m -XX:MaxPermSize=10m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\dump
 * <p>
 * VM Args JDK 1.7: set JAVA_OPTS=-verbose:gc -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\dump
 * <p>
 * JDK 1.8的运行时常量池在元数据区域，该区域的大小只受本机内存限制
 *
 * @author yuhao.wang3
 * @since 2019/11/30 17:09
 */
public class RuntimeConstantOutOfMemoryErrorTest {

    public static void main(String[] args) {
        // 使用List保存着常量池的引用，避免Full GC 回收常量池行为
        List<String> list = new ArrayList<String>();
        for (int i = 0; ; i++) {
            list.add(String.valueOf(i).intern());
        }
    }
}



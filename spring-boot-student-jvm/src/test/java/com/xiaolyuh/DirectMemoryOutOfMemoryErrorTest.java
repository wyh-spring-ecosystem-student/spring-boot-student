package com.xiaolyuh;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * java 直接内存溢出
 * <p>
 * VM Args JDK 1.6: set JAVA_OPTS=-verbose:gc -Xms20m -XX:MaxDirectMemorySize=10m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\dump
 *
 * @author yuhao.wang3
 * @since 2019/11/30 17:09
 */
public class DirectMemoryOutOfMemoryErrorTest {

    public static void main(String[] args) throws IllegalAccessException {
        int _1M = 1024 * 1024;
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1M);
        }
    }
}



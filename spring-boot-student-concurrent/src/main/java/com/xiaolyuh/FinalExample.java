package com.xiaolyuh;

public class FinalExample {
    int i;   // 普通变量
    final int j;   // final变量
    static FinalExample obj;

    public FinalExample() { // 构造函数
        i = 1;// 写普通域
        j = 2;// 写final域
    }

    public static void writer() {   // 写线程A执行
        // 这一步实际上有三个指令，如下：
        // memory = allocate();　　// 1：分配对象的内存空间
        // ctorInstance(memory);　// 2：初始化对象
        // instance = memory;　　// 3：设置instance指向刚分配的内存地址
        obj = new FinalExample();
    }

    public static void reader() {   // 读线程B执行            
        FinalExample object = obj;  // 读对象引用
        int a = object.i; // 读普通域
        int b = object.j; // 读final域
    }
}

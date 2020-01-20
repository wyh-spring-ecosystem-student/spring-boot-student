package com.xiaolyuh;

/**
 * 被动使用类字段演示三：
 * 常量在编译阶段会存入调用类的常量池中，本质上没有直接引用到定义常量的类，因此不会触发定义常量的
 * 类的初始化
 **/
class ConstClass {

    static {
        System.out.println("ConstClass init!");
    }

    public static final String HELLOWORLD = "hello world";
}

/**
 * 非主动使用类字段演示
 **/
public class NotInitialization3 {

    public static void main(String[] args) {
        System.out.println(ConstClass.class.getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());
        System.out.println(ConstClass.HELLOWORLD);
    }
}
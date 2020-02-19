package com.xiaolyuh;

/**
 * 动态态分派
 *
 * @author yuhao.wang3
 */
public class DynamicDispatch {

    static abstract class Human {
        abstract void sayHello();
    }

    static class Man extends Human {
        public void sayHello() {
            System.out.println("hello,Man!");
        }
    }

    static class Woman extends Human {
        public void sayHello() {
            System.out.println("hello,Woman!");
        }
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        man.sayHello();
        woman.sayHello();
    }
}
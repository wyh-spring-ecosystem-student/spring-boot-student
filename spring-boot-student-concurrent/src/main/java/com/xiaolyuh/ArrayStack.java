package com.xiaolyuh;

// 基于数组实现的顺序栈
public class ArrayStack {
    // 数组
    private int elementCount;
    // 栈中元素个数
    private String[] items;
    //栈的大小
    private int size;

    // 初始化数组，申请一个大小为n的数组空间
    public ArrayStack(int size) {
        this.items = new String[size];
        this.size = size;
        this.elementCount = 0;
    }

    // 入栈操作
    public boolean push(String item) {
        // 数组空间不够了，直接返回false，入栈失败。
        if (elementCount == size) {
            return false;
        }
        // 将item放到下标为count的位置，并且count加一
        items[elementCount] = item;
        ++elementCount;
        return true;
    }

    // 出栈操作
    public String pop() {
        // 栈为空，则直接返回null
        if (elementCount == 0) {
            return null;
        }
        // 返回下标为count-1的数组元素，并且栈中元素个数count减一
        String tmp = items[elementCount - 1];
        --elementCount;
        return tmp;
    }
}
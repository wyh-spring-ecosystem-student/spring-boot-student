package com.xiaolyuh;

import java.util.concurrent.locks.ReentrantLock;

public class User {

    private ReentrantLock lock = new ReentrantLock();

    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}
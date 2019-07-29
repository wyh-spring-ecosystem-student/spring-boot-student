package com.xiaolyuh;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuhao.wang3
 * @since 2019/7/26 17:58
 */
public class HashMapTest {
    public static void main(String[] args) {
        AtomicInteger integer = new AtomicInteger();
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(50);
        Map<User, Integer> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 10000; i++) {
            cachedThreadPool.execute(() -> {
                User user = new User(1);
                map.put(user, 1);
            });
        }
    }

    static class User {
        int age;

        public User(int age) {
            this.age = age;
        }

        @Override
        public int hashCode() {
            return age;
        }

        @Override
        public String toString() {
            return "" + age;
        }
    }
}

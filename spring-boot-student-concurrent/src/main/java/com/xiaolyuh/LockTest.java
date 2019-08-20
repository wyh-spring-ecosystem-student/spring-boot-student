package com.xiaolyuh;

import java.util.Random;

/**
 * 死锁示例 示例
 *
 * @author yuhao.wang3
 */
public class LockTest {
    Object lock = new Object();

    public static void main(String[] args) {
        LockTest lockTest = new LockTest();
        User a = new User(), b = new User();
        ThreadTaskUtils.run(() -> lockTest.deadlock(a, b));
        ThreadTaskUtils.run(() -> lockTest.deadlock(b, a));
    }

    /**
     * 死锁解决办法，通过内在排序，保证加锁的顺序性
     *
     * @param a a
     * @param b b
     */
    private void lock1(User a, User b) {
        // 使用原生的HashCode方法，防止hashCode方法被重写导致的一些问题，
        // 如果能确保use对象中的id是唯一且不会重复，可以直接使用userId
        int aHashCode = System.identityHashCode(a);
        int bHashCode = System.identityHashCode(b);

        if (aHashCode > bHashCode) {
            synchronized (a) {
                sleep(1000);
                synchronized (b) {
                    sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " 死锁示例");
                }
            }
        } else if (aHashCode < bHashCode) {
            synchronized (b) {
                sleep(1000);
                synchronized (a) {
                    sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " 死锁示例");
                }
            }
        } else {
            // 引入一个外部锁，解决hash冲突的方法
            synchronized (lock) {
                synchronized (a) {
                    sleep(1000);
                    synchronized (b) {
                        sleep(1000);
                        System.out.println(Thread.currentThread().getName() + " 死锁示例");
                    }
                }
            }
        }
    }


    Random random = new Random();
    /**
     * 使用tryLock尝试拿锁
     *
     * @param a a
     * @param b b
     */
    private void lock2(User a, User b) {
        while (true) {
            try {
                if (a.getLock().tryLock()) {
                    try {
                        if (b.getLock().tryLock()) {
                            System.out.println(Thread.currentThread().getName() + " 死锁示例");
                            break;
                        }
                    } finally {
                        b.getLock().unlock();
                    }
                }
            } finally {
                a.getLock().unlock();
            }
            // 拿锁失败以后，休眠随机数，以避免活锁
            sleep(random.nextInt());
        }
    }

    private void deadlock(User a, User b) {
        synchronized (a) {
            sleep(1000);
            synchronized (b) {
                sleep(1000);
                System.out.println(Thread.currentThread().getName() + " 死锁示例");
            }
        }
    }


    public static void sleep(int probe) {
        try {
            Thread.sleep(probe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


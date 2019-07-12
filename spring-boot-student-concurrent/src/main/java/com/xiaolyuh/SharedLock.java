package com.xiaolyuh;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 共享锁，可以允许多个线程同时获得锁
 *
 * @author yuhao.wang3
 * @since 2019/7/12 10:00
 */
public class SharedLock implements Lock {

    private static class Sync extends AbstractQueuedSynchronizer {

        public Sync(int count) {
            if (count <= 0) {
                throw new IllegalArgumentException("count must large than zero.");
            }
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            int count = getState();
            if (count > 0 && compareAndSetState(count, count - arg)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return count;
            }
            return -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (; ; ) {
                // 通过循环和CAS来保证安全的释放锁
                int count = getState();
                if (compareAndSetState(count, count + arg)) {
                    setExclusiveOwnerThread(null);
                    return true;
                }
            }
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() <= 0;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    private Sync sync;

    /**
     * @param count 能同时获取到锁的线程数
     */
    public SharedLock(int count) {
        this.sync = new Sync(count);
    }

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        try {
            return sync.tryAcquireSharedNanos(1, 100L);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    public static void main(String[] args) {
        final Lock lock = new SharedLock(5);

        // 启动10个线程
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000);
                } catch (Exception e) {
                } finally {
                    lock.unlock();
                }
            }).start();
        }
        // 每隔1秒换行
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println();
        }
    }
}

package com.xiaolyuh;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 互斥锁
 *
 * @author yuhao.wang3
 * @since 2019/7/10 17:21
 */
public class MutexLock implements Lock {

    // 使用静态内部类的方式来自定义同步器，隔离使用者和实现者
    static class Sync extends AbstractQueuedSynchronizer {
        // 我们定义状态标志位是1时表示获取到了锁，为0时表示没有获取到锁
        @Override
        protected boolean tryAcquire(int arg) {
            // 获取锁有竞争所以需要使用CAS原子操作
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            // 只有获取到锁的线程才会解锁，所以这里没有竞争，直接使用setState方法在来改变同步状态
            setState(0);
            setExclusiveOwnerThread(null);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            // 如果货物到锁，当前线程独占
            return getState() == 1;
        }

        // 返回一个Condition，每个condition都包含了一个condition队列
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    // 仅需要将操作代理到Sync上即可
    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryRelease(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(0);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }


    public static void main(String[] args) {
        MutexLock lock = new MutexLock();
        final User user = new User();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    user.setAge(user.getAge() + 1);
                    System.out.println(user.getAge());
                } finally {
                    lock.unlock();
                }
            }).start();
        }
    }
}



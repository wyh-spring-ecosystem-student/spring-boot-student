package com.xiaolyuh;

/**
 * @author yuhao.wang3
 * @since 2019/7/29 21:14
 */
public class ConcurrentLinkedQueueTset {
    public static void main(String[] args) {
        ConcurrentLinkedQueue<Integer> linkedQueue = new ConcurrentLinkedQueue<>();

        linkedQueue.offer(1);
        linkedQueue.offer(2);
        linkedQueue.poll();
        linkedQueue.offer(3);
        linkedQueue.offer(4);
        linkedQueue.poll();
        linkedQueue.poll();
    }
}

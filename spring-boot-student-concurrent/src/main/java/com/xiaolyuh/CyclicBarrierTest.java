package com.xiaolyuh;

import com.alibaba.fastjson.JSON;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier并发工具类
 *
 * @author yuhao.wang3
 * @since 2019/6/27 15:52
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        Random random = new Random();
        Map<String, Long> map = new ConcurrentHashMap<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, () -> {
            System.out.println(Thread.currentThread().getName() + "  3所有线程到达屏障的时候，优先执行barrierAction线程。。。。。。。");
            System.out.println(Thread.currentThread().getName() + "  3" + JSON.toJSONString(map));
        });

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(200 + random.nextInt(200));
                    System.out.println(Thread.currentThread().getName() + "  1等待所有线程到达屏障------------");
                    map.put(Thread.currentThread().getName(), Thread.currentThread().getId());
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName() + "  2所有线程到达屏障的时候，开始执行业务代码================");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
        }

        try {
            cyclicBarrier.await();
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + "  主线程完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

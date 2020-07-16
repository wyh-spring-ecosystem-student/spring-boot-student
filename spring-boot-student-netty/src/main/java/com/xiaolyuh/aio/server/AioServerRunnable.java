package com.xiaolyuh.aio.server;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author olafwang
 * @since 2020/6/22 9:53 上午
 */
public class AioServerRunnable implements Runnable {

    private AsynchronousServerSocketChannel socketChannel;
    private CountDownLatch latch = new CountDownLatch(1);

    public AioServerRunnable(AsynchronousServerSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        //用于接收客户端的连接，异步操作，
        // 需要实现了CompletionHandler接口的处理器处理和客户端的连接操作
        socketChannel.accept(this, new AioAcceptHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public AsynchronousServerSocketChannel getSocketChannel() {
        return socketChannel;
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}

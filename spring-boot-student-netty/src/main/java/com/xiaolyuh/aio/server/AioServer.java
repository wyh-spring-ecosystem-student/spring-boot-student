package com.xiaolyuh.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author olafwang
 * @since 2020/6/22 9:47 上午
 */
public class AioServer {
    public static final AtomicInteger clientCount = new AtomicInteger();

    public static void main(String[] args) {
        try {
            //创建服务端通道
            AsynchronousServerSocketChannel socketChannel = AsynchronousServerSocketChannel.open();
            //绑定端口
            socketChannel.bind(new InetSocketAddress(4700));
            System.out.println("服务端已经启动，端口号:" + 4700);
            new Thread(new AioServerRunnable(socketChannel), "aio-server-thread").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

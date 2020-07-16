package com.xiaolyuh.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 新建连接的回调处理器
 *
 * @author olafwang
 * @since 2020/6/22 9:53 上午
 */
public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServerRunnable> {
    @Override
    public void completed(AsynchronousSocketChannel channel, AioServerRunnable serverHandler) {
        int clientCount = AioServer.clientCount.incrementAndGet();
        System.out.println("连接的客户端数：" + clientCount);
        //重新注册监听，让别的客户端也可以连接
        serverHandler.getSocketChannel().accept(serverHandler, this);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        //1)ByteBuffer dst：接收缓冲区，用于从异步Channel中读取数据包；
        //2)  A attachment：异步Channel携带的附件，通知回调的时候作为入参使用；
        //3)  CompletionHandler<Integer,? super A>：系统回调的业务handler，进行读操作
        channel.read(readBuffer, readBuffer, new AioReadHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AioServerRunnable serverHandler) {
        exc.printStackTrace();
        serverHandler.getLatch().countDown();
    }

}

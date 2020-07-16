package com.xiaolyuh.aio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 读取数据的异步回调处理器
 *
 * @author olafwang
 * @since 2020/6/22 11:09 上午
 */
public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;

    public AioReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    /**
     * 读取到消息后的回调处理
     *
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        //如果条件成立，说明客户端主动终止了TCP套接字，这时服务端终止就可以了
        if (result == -1) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        //flip操作
        attachment.flip();
        byte[] message = new byte[attachment.remaining()];
        attachment.get(message);
        try {
            System.out.println(result);
            String msg = new String(message, "UTF-8");
            System.out.println("收到客户端消息：" + msg);
            String responseStr = "客户端收到了你的信息：" + msg;
            //向客户端发送消息
            doWrite(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String msg) {
        //发送消息
        byte[] bytes = msg.getBytes();
        // 创建缓冲区
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        // 将缓存字节数组的指针设置为数组的开始序列即数组下标0。这样就可以从buffer开头，对该buffer进行遍历（读取）了。
        // 如果不调用该方法，那么就将从文件最后开始读取的，然后读出来的数据都是byte=0的字符。

        // buffer中的flip方法涉及到bufer中的Capacity,Position和Limit三个概念。
        // 其中Capacity在读写模式下都是固定的，就是我们分配的缓冲大小,Position类似于读写指针，
        // 表示当前读(写)到什么位置,Limit在写模式下表示最多能写入多少数据，此时和Capacity相同，
        // 在读模式下表示最多能读多少数据，此时和缓存中的实际数据大小相同。
        // 在写模式下调用flip方法，那么limit就设置为了position当前的值(即当前写了多少数据),postion会被置为0，以表示读操作从缓存的头开始读。
        // 也就是说调用flip之后，读写指针指到缓存头部，并且设置了最多只能读出之前写入的数据长度(而不是整个缓存的容量大小)。
        writeBuffer.flip();
        //异步写数据
        channel.write(writeBuffer, writeBuffer,
                new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        if (attachment.hasRemaining()) {
                            channel.write(attachment, attachment, this);
                        } else {
                            //读取客户端传回的数据
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            //异步读数据
                            channel.read(readBuffer, readBuffer,
                                    new AioReadHandler(channel));
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

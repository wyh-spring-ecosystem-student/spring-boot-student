package com.xiaolyuh.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author olafwang
 * @since 2020/6/15 2:51 下午
 */
public class SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClient.class);

    public static void main(String[] args) throws IOException {
        // host即客户端需要连接的机器，port就是服务器端用来监听请求的端口
        String host = "127.0.0.1";
        int port = 9999;
        try {
            Socket client = new Socket(host, port);
            Writer writer = new OutputStreamWriter(client.getOutputStream());
            writer.write("Hello Socket");
            writer.flush();
            writer.close();
            client.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

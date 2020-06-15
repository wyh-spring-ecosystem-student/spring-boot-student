package com.xiaolyuh.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author olafwang
 * @since 2020/6/15 2:51 下午
 */
public class SocketServer {
    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    public static void main(String[] args) throws IOException {
        try {
            // 开启有个服务端的Socket，在选择端口时，需要注意一点，就是0~1023这些端口都已经被系统预留了。
            ServerSocket echoServer = new ServerSocket(9999);
            // 从ServerSocket创建一个Socket对象用来监听和接受连接。
            Socket clientSocket = echoServer.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // 应答客户端
            PrintStream os = new PrintStream(clientSocket.getOutputStream());
            String line;
            // 将数据回现客户端
            while (true) {
                line = br.readLine();
                if (!StringUtils.isEmpty(line)) {
                    System.out.println("收到客户端数据：" + line);
                    os.println("服务端的响应数据：" + line);
                    os.close();
                    br.close();
                    clientSocket.close();
                    break;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

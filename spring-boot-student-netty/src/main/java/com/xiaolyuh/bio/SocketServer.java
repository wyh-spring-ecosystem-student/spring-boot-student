package com.xiaolyuh.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author olafwang
 * @since 2020/6/15 2:51 下午
 */
public class SocketServer {
    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    public static void main(String[] args) throws IOException {
        //创建一个ServerSocket在端口4700监听客户请求
        try (ServerSocket server = new ServerSocket(4700)) {
            while (true) {
                //使用accept()阻塞等待客户请求，有客户, 请求到来则产生一个Socket对象，并继续执行
                Socket socket = server.accept();

                ExecutorService executor = Executors.newFixedThreadPool(10);
                executor.submit(() -> {
                    try (//由Socket对象得到输入流，并构造相应的BufferedReader对象
                         BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         //由Socket对象得到输出流，并构造PrintWriter对象
                         PrintWriter os = new PrintWriter(socket.getOutputStream());) {

                        String line = is.readLine();
                        while (!"bye".equals(line) && line != null) {
                            //在标准输出上打印从客户端读入的字符串
                            System.out.println(Thread.currentThread().getName() + " 收到客户端信息:" + line);

                            //向客户端输出该字符串
                            os.println("服务端收到了你的消息：" + line);
                            //刷新输出流，使Client马上收到该字符串
                            os.flush();
                            line = is.readLine();
                        }
                        // 关闭连接
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}

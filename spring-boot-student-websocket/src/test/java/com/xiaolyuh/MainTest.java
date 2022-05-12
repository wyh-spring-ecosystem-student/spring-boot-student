package com.xiaolyuh;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MainTest {
    public static void main(String[] args) throws Exception {
        // ws = http  wss=https
        WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://localhost:8081/runninglog/146tp6w0wjs"), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("[websocket] 连接成功");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("[websocket] 收到消息={}" + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("[websocket] 退出连接");
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("[websocket] 连接错误={}" + ex.getMessage());
            }
        };
        webSocketClient.connect();
        webSocketClient.send("斤斤计较斤斤计较斤斤计较");
        webSocketClient.isFlushAndClose();
        Thread.sleep(10000L);
    }
}

package com.xiaolyuh.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规划会导入表格进度查询
 *
 * @author wyh
 */
@ServerEndpoint("/runninglog/{key}")
@Slf4j
@Controller
public class WebSocketServerEndpoint {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
     */
    private static ConcurrentHashMap<String, Session> SESSION_POOLS = new ConcurrentHashMap<>();

    /**
     * 新的WebSocket请求开启
     */
    @OnOpen
    public void onOpen(@PathParam("key") String key, Session session) {
        SESSION_POOLS.put(key, session);
        log.info("[WebSocket] 连接成功，当前连接人数为：={}", SESSION_POOLS.size());
        try {
            session.getBasicRemote().sendText("Web socket 消息");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * WebSocket请求关闭
     */
    @OnClose
    public void onClose(@PathParam("key") String key) {
        SESSION_POOLS.remove(key);
        log.info("WebSocket请求关闭");
    }

    @OnError
    public void onError(Throwable thr) {
        log.info("WebSocket 异常");
    }

    /**
     * 收到客户端信息
     */
    @OnMessage
    public void onMessage(@PathParam("key") String key, String message, Session session) throws IOException {
        message = "客户端：" + message + ",已收到";
        session.getBasicRemote().sendText("Web socket 消息：：" + message);
        log.info(message);
    }

}
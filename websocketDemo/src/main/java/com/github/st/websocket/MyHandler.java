package com.github.st.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class MyHandler extends TextWebSocketHandler {
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {

        session.sendMessage(new TextMessage("消息已收到" + message.getPayload()));

        if (message.getPayload().equals("10")) {
            for (int i = 0; i < 10; i++) {
                session.sendMessage(new TextMessage("消息 -> " + i));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws
            Exception {
        System.out.println("uid => " + session.getAttributes().get("uid"));
        session.sendMessage(new TextMessage("欢迎连接到ws服务"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        System.out.println("断开连接！");
    }
}

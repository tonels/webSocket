package com.github.ws1.controller;

import com.github.ws1.socket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/msg/")
public class PageController {

    @Autowired
    private WebSocketServer server;

    /**
     * 推送消息给客户端
     *
     * @param msg   消息内容
     * @param token 用户标识，为空时给所有用户推送
     * @return
     */
    @RequestMapping(value = "sendMsg")
    public String sendMsg(String msg, String token) {
        if (token == null) {
            server.batchSendMessage(msg);
        } else {
            server.SendMessage(token, msg);
        }
        return "ok";
    }

}

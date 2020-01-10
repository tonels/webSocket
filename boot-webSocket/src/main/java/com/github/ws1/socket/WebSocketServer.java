package com.github.ws1.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/socket/{token}")
@Component
public class WebSocketServer {

	// 	加入日志
	private Logger log = LoggerFactory.getLogger(getClass());
	// 	用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	// 	存储用户唯一	可改用缓存
	private static Map<String, Session> socketMap = new HashMap<String, Session>();
	// 	当前线程
	private Session session;
	// 	当前用户标识
	private String token;

	/**
	 * 链接启用
	 * 
	 * @param session
	 * @param token
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "token") String token) {
		// 	用户信息标识添加
		this.session = session;
		//	可用来当成唯一标识
		this.token = token;
		// 	加入缓存
		//	原来存在这个token用户,则剔除用户
		Session temp = socketMap.get(token);
		if (temp != null && temp.isOpen()) {
			try {
				temp.close();
			} catch (IOException e) {
				log.info("用户:" + token + ",剔除失败!");
				e.printStackTrace();
			}
		}
		socketMap.put(token, session);
		// 更新在线人数 TODO	正式需使用线程安全记录
		WebSocketServer.onlineCount ++;

		log.info("有新窗口开始监听:" + token + ",当前在线人数为" + onlineCount);

	}

	/**
	 * 关闭链接
	 */
	@OnClose
	public void onClose() {
		// 从线程中移除
		socketMap.remove(this.token);
		// 更新在线人数 TODO	正式需使用线程安全记录
		WebSocketServer.onlineCount --;
		
		log.info("有一连接关闭！当前在线人数为" + onlineCount);
	}

	/**
	 * 当链接出现异常
	 */
	@OnError
	public void onError(Session session, Throwable ex) {
		log.error("发生错误：{}，Session ID： {}", ex.getMessage(), session.getId());
	}

	/**
	 * 接收客户端发来的信息
	 * 
	 * @param message
	 */
	@OnMessage
	public void onMessage(String message) {
		log.info("收到来自窗口" + token + "的信息:" + message);
		//	回复收到信息
		this.SendMessage(session, "收到消息；" + message);

	}
	
	/**
	 * 	给指定客户端发送消息
	 * @param session
	 * @param message
	 */
	public void SendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));
		}catch (Exception e) {
			log.error("发送消息出错", e);
		}
	}
	
	/**
	 * 	群发消息
	 * @param message
	 */
	public void batchSendMessage(String message) {
		for (Entry<String, Session> entry : socketMap.entrySet()) {
			Session session = entry.getValue();
			if (session.isOpen()) {
				SendMessage(session, message);
			}else {//	已经关闭则移除
				socketMap.remove(entry.getKey());
			}
		}
	}
	
	
	/**
	 * 	给指定用户发送信息
	 * @param token
	 * @param message
	 */
	public void SendMessage(String token, String message) {
		Session session = socketMap.get(token);
		if (session != null) {
			SendMessage(session, message);
		}
	}
}

package com.github.st.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class websocketconfig implements WebSocketConfigurer {

    @Autowired
    private MyHandshakeInterceptor myHandshakeInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler
                (myHandler(),"/ws").setAllowedOrigins("*").addInterceptors(myHandshakeInterceptor);
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }
}

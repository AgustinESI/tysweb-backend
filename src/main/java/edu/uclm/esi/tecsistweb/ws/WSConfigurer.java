package edu.uclm.esi.tecsistweb.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WSConfigurer implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.
                addHandler(new MatchesWebSocket(), "/ws-matches").
                setAllowedOrigins("*").
                addInterceptors(new HttpSessionHandshakeInterceptor()).

                addHandler(new MatchesWebSocket(), "/ws-chat").
                setAllowedOrigins("*").
                addInterceptors(new HttpSessionHandshakeInterceptor());
    }
}

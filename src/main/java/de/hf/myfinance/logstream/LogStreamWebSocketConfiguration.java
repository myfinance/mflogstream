package de.hf.myfinance.logstream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
public class LogStreamWebSocketConfiguration {

    @Bean
    SimpleUrlHandlerMapping simpleUrlHandlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping(Map.of("/ws/logs", wsh), 10);
    }

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}

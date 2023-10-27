package de.hf.myfinance.logstream;

import org.springframework.web.reactive.socket.WebSocketSession;

import java.time.LocalDateTime;

public record SessionWrapper(WebSocketSession session, LocalDateTime startTime) {
}

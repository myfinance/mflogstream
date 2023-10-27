package de.hf.myfinance.logstream;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LogStreamWebSocketHandler implements WebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


    public LogStreamWebSocketHandler() {
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
        var receive = session
                .receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(this::createResponse)
                .map(session::textMessage);
        return session.send(receive);
    }

    private Flux<String> createResponse(String msg) {
        return Flux.just(LocalDateTime.now() + ": Logstream successfuly connected for user:" + msg);
    }

    public void broadcastMessage(String message) {
        for (WebSocketSession session : sessions.values()) {
            session.send(Mono.just(session.textMessage(message)))
                    .onErrorResume(e -> Mono.empty())
                    .subscribe();
        }
    }
}

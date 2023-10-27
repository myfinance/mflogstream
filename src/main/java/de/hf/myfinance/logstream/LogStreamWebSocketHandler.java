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

    private final Map<String, SessionWrapper> sessions = new ConcurrentHashMap<>();
    private final int sessionTimeout = 180; //Timeout in minutes


    public LogStreamWebSocketHandler() {
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        sessionCleanup();
        String sessionId = session.getId();
        sessions.put(sessionId, new SessionWrapper(session, LocalDateTime.now()));
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
        sessionCleanup();
        for (SessionWrapper session : sessions.values()) {
            session.session().send(Mono.just(session.session().textMessage(message)))
                    .onErrorResume(e -> Mono.empty())
                    .subscribe();
        }
    }

    private void sessionCleanup() {
        LocalDateTime ts = LocalDateTime.now().minusMinutes(sessionTimeout);
        for (SessionWrapper session : sessions.values()) {
            if(session.startTime().isBefore(ts)) {
                sessions.remove(session.session().getId());
            }
        }
    }
}

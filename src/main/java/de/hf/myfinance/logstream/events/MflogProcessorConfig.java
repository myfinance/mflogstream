package de.hf.myfinance.logstream.events;

import de.hf.framework.audit.AuditService;
import de.hf.framework.audit.Severity;
import de.hf.myfinance.event.Event;
import de.hf.myfinance.logstream.LogStreamWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
public class MflogProcessorConfig {
    private final AuditService auditService;
    protected static final String AUDIT_MSG_TYPE="MflogProcessorC_Event";

    private LogStreamWebSocketHandler logStreamWebSocketHandler;

    @Autowired
    public MflogProcessorConfig(LogStreamWebSocketHandler logStreamWebSocketHandler, AuditService auditService) {
        this.logStreamWebSocketHandler = logStreamWebSocketHandler;
        this.auditService = auditService;
    }

    @Bean
    public Consumer<Event<String, String>> mflogProcessor() {
        return event -> {
            auditService.saveMessage("Process message created at:"+event.getEventCreatedAt(), Severity.DEBUG, AUDIT_MSG_TYPE);
            switch (event.getEventType()) {

                case CREATE:
                    auditService.saveMessage("msg with ID:"+event.getKey(), Severity.DEBUG, AUDIT_MSG_TYPE);
                    auditService.saveMessage("msg:"+event.getData(), Severity.DEBUG, AUDIT_MSG_TYPE);
                    this.logStreamWebSocketHandler.broadcastMessage(event.getKey() + ":" + event.getData());
                    break;

                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE event";
                    auditService.saveMessage(errorMessage, Severity.WARN, AUDIT_MSG_TYPE);
            }

            auditService.saveMessage("Message processing done!", Severity.DEBUG, AUDIT_MSG_TYPE);

        };
    }
}

package de.hf.myfinance.logstream.events;

import de.hf.myfinance.event.Event;
import de.hf.myfinance.logstream.LogStreamWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
public class MflogProcessorConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MflogProcessorConfig.class);

    private LogStreamWebSocketHandler logStreamWebSocketHandler;

    @Autowired
    public MflogProcessorConfig(LogStreamWebSocketHandler logStreamWebSocketHandler) {
        this.logStreamWebSocketHandler = logStreamWebSocketHandler;
    }

    @Bean
    public Consumer<Event<String, String>> mflogProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    LOG.info("msg with ID: {}", event.getKey());
                    LOG.info("msg: {}", event.getData());
                    this.logStreamWebSocketHandler.broadcastMessage(event.getKey() + ":" + event.getData());
                    break;

                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE event";
                    LOG.warn(errorMessage);
            }

            LOG.info("Message processing done!");

        };
    }
}

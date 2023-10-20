package de.hf.myfinance.logstream;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class LogStreamService {

    private final Sinks.Many<LogStreamResponse> dataSink;

    public LogStreamService() {
        this.dataSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void addData(String data) {
        dataSink.tryEmitNext(new LogStreamResponse(data, "Info"));
    }
    Flux<LogStreamResponse> publishLog(LogStreamRequest request) {
        return dataSink.asFlux();
    }

}

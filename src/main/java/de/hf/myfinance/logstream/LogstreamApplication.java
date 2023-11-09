package de.hf.myfinance.logstream;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("de.hf")
public class LogstreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogstreamApplication.class, args);
	}

}


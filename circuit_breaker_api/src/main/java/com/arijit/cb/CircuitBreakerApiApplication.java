package com.arijit.cb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CircuitBreakerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerApiApplication.class, args);
	}

}

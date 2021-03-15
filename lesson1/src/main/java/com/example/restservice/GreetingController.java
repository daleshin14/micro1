package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	private static final String TEMPLATE = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping
	public ResponseEntity<?> ok() {
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}
	
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
	}
}

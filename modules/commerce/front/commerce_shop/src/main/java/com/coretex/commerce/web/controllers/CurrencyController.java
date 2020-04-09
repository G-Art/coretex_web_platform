package com.coretex.commerce.web.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/v1/currencies")
public class CurrencyController {

	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	private Flux<String> getAll() {
		return Flux.just("1", "2", "3").delayElements(Duration.ofSeconds(1));
	}
}

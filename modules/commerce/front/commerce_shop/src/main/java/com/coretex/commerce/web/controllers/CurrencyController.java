package com.coretex.commerce.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/currencies")
public class CurrencyController {

	@GetMapping
	private Flux<String> getAll() {
		return Flux.just("1", "2", "3");
	}
}

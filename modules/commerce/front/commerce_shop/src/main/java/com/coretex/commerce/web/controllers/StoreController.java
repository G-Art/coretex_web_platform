package com.coretex.commerce.web.controllers;

import com.coretex.commerce.data.StoreData;
import com.coretex.commerce.facades.StoreFacade;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static org.apache.http.client.utils.URIUtils.extractHost;

@RestController
@RequestMapping("/v1/stores")
public class StoreController {

	@Resource
	private StoreFacade storeFacade;


	@GetMapping(path = "/current")
	public Mono<StoreData> getByDomain(ServerWebExchange exchange) {
		var request = exchange.getRequest();

		var domain = extractHost(request.getURI()).getHostName();
		return Mono.justOrEmpty(storeFacade.getByDomain(domain))
				.defaultIfEmpty(storeFacade.getByCode("DEFAULT"));
	}

	@GetMapping
	public Flux<StoreData> getAll() {
		return Flux.fromStream(storeFacade::getAll);
	}

}

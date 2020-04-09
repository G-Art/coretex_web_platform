package com.coretex.commerce.web.controllers;

import com.coretex.commerce.config.security.service.SessionManager;
import com.coretex.commerce.data.CartData;
import com.coretex.commerce.facades.CartFacade;
import com.coretex.commerce.web.data.AddToCartRequest;
import com.coretex.commerce.web.data.UpdateCartEntryRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static org.apache.http.client.utils.URIUtils.extractHost;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

	@Resource
	private CartFacade cartFacade;

	@Resource
	private SessionManager sessionManager;

	@GetMapping(path = "/current")
	private Mono<CartData> getCurrent(ServerWebExchange exchange) {
		var request = exchange.getRequest();

		var domain = extractHost(request.getURI()).getHostName();
		return sessionManager.getCurrentCartUUID(exchange)
				.map(cartUUID -> cartUUID
						.map(uuid -> cartFacade.getByUUID(uuid))
						.orElseGet(() -> {
							var cartData = cartFacade.createCart(domain);
							sessionManager.setCurrentCartUUID(exchange, cartData);
							return cartData;
						}));
	}


	@PostMapping(path = "/add")
	private Mono<CartData> addToCart(ServerWebExchange exchange, @RequestBody AddToCartRequest request) {
		return getCurrent(exchange)
				.map(cartData -> cartFacade.addToCart(cartData, request.getProduct(), request.getQuantity()));
	}

	@PostMapping(path = "/update")
	private Mono<CartData> addToCart(ServerWebExchange exchange, @RequestBody UpdateCartEntryRequest request) {
		return getCurrent(exchange)
				.map(cartData -> cartFacade.updateCart(cartData, request.getEntry(), request.getQuantity()));
	}
}

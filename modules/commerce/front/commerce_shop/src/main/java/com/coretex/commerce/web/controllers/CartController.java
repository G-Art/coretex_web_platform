package com.coretex.commerce.web.controllers;

import com.coretex.commerce.config.security.service.SessionManager;
import com.coretex.commerce.data.CartData;
import com.coretex.commerce.facades.CartFacade;
import com.coretex.commerce.web.data.AddToCartRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

	@Resource
	private CartFacade cartFacade;

	@Resource
	private SessionManager sessionManager;

	@GetMapping(path = "/current")
	private Mono<CartData> getCurrent(ServerWebExchange exchange)
	{
		var cartUUID = sessionManager.getCurrentCartUUID(exchange);
		CartData cartData;
		if(Objects.isNull(cartUUID)){
			cartData = cartFacade.createCart();

			sessionManager.setCurrentCartUUID(exchange, cartData);
		}else {
			cartData = cartFacade.getByUUID(cartUUID);
		}
		return Mono.just(cartData);
	}


	@PostMapping(path = "/add")
	private Mono<CartData> addToCart(ServerWebExchange exchange, @RequestBody AddToCartRequest request)
	{
		return getCurrent(exchange)
				.map( cartData ->  cartFacade.addToCart(cartData, request.getProduct(), request.getQuantity()));
	}
}

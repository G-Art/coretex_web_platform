package com.coretex.commerce.web.controllers;

import com.coretex.commerce.config.security.service.SessionManager;
import com.coretex.commerce.data.CartData;
import com.coretex.commerce.data.OrderPlaceResult;
import com.coretex.commerce.facades.CartFacade;
import com.coretex.commerce.helpers.CartHelper;
import com.coretex.commerce.web.data.AddToCartRequest;
import com.coretex.commerce.web.data.SetEntryRequest;
import com.coretex.commerce.web.data.UpdateCartEntryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

	@Resource
	private CartHelper cartHelper;

	@Resource
	private CartFacade cartFacade;

	@Resource
	private SessionManager sessionManager;

	@GetMapping(path = "/current")
	public Mono<CartData> getCurrent(ServerWebExchange exchange) {
		return sessionManager.getCurrentCartUUID(exchange)
				.flatMap(cartUUID -> cartUUID
						.map(uuid -> cartHelper.cartForCustomer(exchange, uuid))
						.orElseGet(() -> cartHelper.cartForCustomer(exchange, null)));
	}

	@PostMapping(path = "/add")
	public Mono<CartData> addToCart(ServerWebExchange exchange, @RequestBody AddToCartRequest request) {
		return getCurrent(exchange)
				.map(cartData -> cartFacade.addToCart(cartData, request.getProduct(), request.getQuantity()));
	}

	@PostMapping(path = "/update")
	public Mono<CartData> addToCart(ServerWebExchange exchange, @RequestBody UpdateCartEntryRequest request) {
		return getCurrent(exchange)
				.map(cartData -> cartFacade.updateCart(cartData, request.getEntry(), request.getQuantity()));
	}

	@PostMapping(path = "/delivery/type")
	public Mono<CartData> setDeliveryType(ServerWebExchange exchange, @RequestBody SetEntryRequest request) {
		return getCurrent(exchange)
				.map(cartData -> cartFacade.setDeliveryType(cartData, request.getUuid()));
	}

	@PostMapping(path = "/payment/type")
	public Mono<CartData> setPaymentType(ServerWebExchange exchange, @RequestBody SetEntryRequest request) {
		return getCurrent(exchange)
				.map(cartData -> cartFacade.setPaymentType(cartData, request.getUuid()));
	}

	@PostMapping(path = "/delivery/info")
	public Mono<CartData> saveDeliveryInfo(ServerWebExchange exchange, @RequestBody Map<String, Object> info) {
		return getCurrent(exchange)
				.map(cartData -> cartFacade.saveDeliveryInfo(cartData, info));
	}

	@PostMapping(path = "/placeOrder")
	public Mono<ResponseEntity<OrderPlaceResult>> placeOrder(ServerWebExchange exchange) {
		return getCurrent(exchange)
				.map(cartData -> cartFacade.palaceOrder(cartData))
				.map(o -> ResponseEntity.status(HttpStatus.OK).body(o));
	}
}

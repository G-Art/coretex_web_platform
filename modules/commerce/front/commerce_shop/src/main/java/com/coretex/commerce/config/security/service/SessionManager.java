package com.coretex.commerce.config.security.service;

import com.coretex.commerce.data.CartData;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionManager {

	private static final String SESSION_ATTRIBUTE = "sessionAttribute[%s]";

	public static final String SESSION_CART_UUID = String.format(SESSION_ATTRIBUTE, "sessionCartUUID");

	public Mono<Optional<UUID>> getCurrentCartUUID(ServerWebExchange exchange) {
		return exchange.getSession().map(webSession -> Optional.ofNullable(webSession.getAttribute(SESSION_CART_UUID)));
	}

	public void setCurrentCartUUID(ServerWebExchange exchange, CartData cartData) {
		if (Objects.isNull(cartData)) {
			exchange.getSession()
					.subscribe(webSession -> webSession.getAttributes().remove(SESSION_CART_UUID));
		} else {
			exchange.getSession()
					.subscribe(webSession -> webSession.getAttributes().put(SESSION_CART_UUID, cartData.getUuid()));
		}
	}
}

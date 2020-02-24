package com.coretex.commerce.config.security.service;

import com.coretex.commerce.data.CartData;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.UUID;

@Service
public class SessionManager {

	private static final String SESSION_ATTRIBUTE = "sessionAttribute[%s]";

	public static final String SESSION_CART_UUID = String.format(SESSION_ATTRIBUTE, "sessionCartUUID");

	public UUID getCurrentCartUUID(ServerWebExchange exchange) {
		return exchange.getSession()
				.block().getAttribute(SESSION_CART_UUID);
	}

	public void setCurrentCartUUID(ServerWebExchange exchange, CartData cartData) {
		exchange.getSession().subscribe(webSession -> {
			webSession.getAttributes().put(SESSION_CART_UUID, cartData.getUuid());
		});
	}
}

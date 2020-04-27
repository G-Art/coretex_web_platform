package com.coretex.commerce.helpers;

import com.coretex.commerce.config.security.service.JWTUserService;
import com.coretex.commerce.config.security.service.SessionManager;
import com.coretex.commerce.data.CartData;
import com.coretex.commerce.facades.CartFacade;
import com.coretex.items.cx_core.CustomerItem;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.function.Function;

import static org.apache.http.client.utils.URIUtils.extractHost;

@Component
public class CartHelper {

	@Resource
	private CartFacade cartFacade;

	@Resource
	private SessionManager sessionManager;

	@Resource
	private JWTUserService jwtUserService;

	public Mono<CartData> cartForCustomer(ServerWebExchange exchange, UUID cartUUID) {
		var request = exchange.getRequest();
		var domain = extractHost(request.getURI()).getHostName();
		return Mono.fromSupplier(() -> jwtUserService.getCurrentUser(exchange)
				.map(customer -> {
					var sessionCart = getOrCreateUserCart(cartUUID, customer, domain);
					cartFacade.getCartsForCustomer(customer.getUuid())
							.filter(userCart -> !userCart.getUuid().equals(sessionCart.getUuid()))
							.forEach(userCart -> cartFacade.merge(sessionCart, userCart));
					sessionManager.setCurrentCartUUID(exchange, sessionCart);
					return sessionCart;
				}).switchIfEmpty(
						Mono.fromSupplier(() -> {
							var sessionCart = getOrCreateUserCart(cartUUID, null, domain);
							sessionManager.setCurrentCartUUID(exchange, sessionCart);
							return sessionCart;
						})
				)
		).flatMap(Function.identity());
	}

	private CartData getOrCreateUserCart(UUID cartUUID, CustomerItem customer, String domain) {
		return cartFacade.getOrCreateCart(cartUUID, domain, customer);
	}


}

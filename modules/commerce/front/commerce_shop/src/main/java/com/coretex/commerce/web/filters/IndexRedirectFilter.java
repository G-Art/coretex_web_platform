package com.coretex.commerce.web.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class IndexRedirectFilter implements WebFilter {


	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		var request = exchange.getRequest();
		if (!request.getPath().pathWithinApplication().value().startsWith("/v1") &&
				!request.getPath().pathWithinApplication().value().startsWith("/newpost") &&
				!request.getPath().pathWithinApplication().value().startsWith("/app")) {
			return chain.filter(
					exchange.mutate()
							.request(
									request.mutate()
											.path("/app/index.html")
											.build()
							).build()
			);
		} else {
			return chain.filter(exchange);
		}
	}
}

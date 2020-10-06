package com.coretex.commerce.web.filters;

import com.coretex.commerce.config.ReactiveRequestContextHolder;
import com.coretex.commerce.helpers.RequestContextLoadProcessor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import javax.annotation.Resource;

@Component
public class RequestContextFilter implements WebFilter {

	@Resource
	private RequestContextLoadProcessor requestContextLoadProcessor;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(exchange)
				.subscriberContext(ctx -> loadContext(ctx, exchange));
	}

	public Context loadContext(Context context, ServerWebExchange exchange) {
		return context.hasKey(SecurityContext.class) ? context :
				context.put(ReactiveRequestContextHolder.CONTEXT_KEY, requestContextLoadProcessor.load(exchange));
	}
}

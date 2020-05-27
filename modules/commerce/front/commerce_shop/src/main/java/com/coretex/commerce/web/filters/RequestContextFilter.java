package com.coretex.commerce.web.filters;

import com.coretex.commerce.config.ReactiveRequestContextHolder;
import com.coretex.commerce.helpers.RequestContextLoadProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import javax.annotation.Resource;
import java.util.Map;

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
		context.<Map<String, Object>>getOrEmpty(ReactiveRequestContextHolder.CONTEXT_KEY)
				.ifPresentOrElse(m -> m.putAll(requestContextLoadProcessor.load(exchange)),
						() -> context.put(ReactiveRequestContextHolder.CONTEXT_KEY, requestContextLoadProcessor.load(exchange)));
		return context;
	}
}

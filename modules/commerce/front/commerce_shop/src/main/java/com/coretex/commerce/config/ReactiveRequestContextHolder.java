package com.coretex.commerce.config;

import com.coretex.commerce.web.loaders.impl.LocaleRequestContextAttributeLoader;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Map;

public class ReactiveRequestContextHolder {
	public static final String CONTEXT_KEY = "R-CONTEXT";

	@SuppressWarnings("unchecked")
	public static <T> Mono<T> getContextAttribute(String key) {
		return getContextAllAttributes()
				.map(map -> (T)map.get(key));
	}

	public static Mono<Map<String, Object>> getContextAllAttributes() {
		return Mono.subscriberContext()
				.map(ctx -> ctx.get(CONTEXT_KEY));
	}

	public static Mono<Locale> getCurrentLocale(){
		return getContextAttribute(LocaleRequestContextAttributeLoader.CONTEXT_LOCALE_KEY);
	}
}

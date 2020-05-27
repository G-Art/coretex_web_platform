package com.coretex.commerce.web.loaders.impl;

import com.coretex.commerce.web.loaders.RequestContextAttributeLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Locale;
import java.util.Map;

@Component
public class LocaleRequestContextAttributeLoader implements RequestContextAttributeLoader {

	public static final String CONTEXT_LOCALE_KEY = "R-CONTEXT::LOCALE::";

	@Override
	public void load(ServerWebExchange exchange, Map<String, Object> attributeMap) {
		Locale current = Locale.ENGLISH;

		attributeMap.put(CONTEXT_LOCALE_KEY, current);
	}
}

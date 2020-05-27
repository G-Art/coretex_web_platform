package com.coretex.commerce.web.loaders;

import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

public interface RequestContextAttributeLoader {
	void load(ServerWebExchange exchange, Map<String, Object> attributeMap);
}

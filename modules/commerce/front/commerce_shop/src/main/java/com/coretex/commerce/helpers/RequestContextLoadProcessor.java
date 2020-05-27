package com.coretex.commerce.helpers;

import com.coretex.commerce.web.loaders.RequestContextAttributeLoader;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Map;

@Component
public class RequestContextLoadProcessor {

	private List<RequestContextAttributeLoader> requestContextAttributeLoaders = null;

	public RequestContextLoadProcessor(List<RequestContextAttributeLoader> requestContextAttributeLoaders) {
		this.requestContextAttributeLoaders = requestContextAttributeLoaders;
	}

	public Map<? extends String, ?> load(ServerWebExchange exchange) {
		Map<String, Object> result = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(requestContextAttributeLoaders)) {
			requestContextAttributeLoaders.forEach(requestContextAttributeLoader -> requestContextAttributeLoader.load(exchange, result));
		}
		return result;
	}
}

package com.coretex.commerce.web.resolvers;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParamMapHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(GroupedParametersMap.class) && Group.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
		var request = exchange.getRequest();
		GroupedParametersMap groupedParametersMap = parameter.getParameterAnnotation(GroupedParametersMap.class);

		var parameterMap = request.getQueryParams();

		return Mono.fromSupplier(() -> {
			Map<String, List<String>> resultMap = new HashMap<>();
			Set<String> mappedParams = parameterMap.keySet()
					.stream()
					.filter(key -> key.matches(String.format("^(%s){1}(\\(){1}((\\_|[a-zA-Z])\\w*)(\\)){1}$", groupedParametersMap.value())))
					.collect(Collectors.toSet());

			if (CollectionUtils.isNotEmpty(mappedParams)) {
				mappedParams.forEach(param -> {
					var strings = parameterMap.get(param);
					var resultList = strings.stream()
							.flatMap(s -> Stream.of(s.split(",")))
							.collect(Collectors.toList());
					resultMap.put(computeKey(param), resultList);
				});
			}

			return new Group(resultMap);
		});

	}


	private String computeKey(String param) {
		return StringUtils.substringBetween(param, "(", ")");
	}
}
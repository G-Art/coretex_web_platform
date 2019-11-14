package com.coretex.core.tests.extentions;

import java.lang.reflect.Parameter;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.mockito.Mock;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class MockitoExtension implements TestInstancePostProcessor, ParameterResolver {

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter().isAnnotationPresent(Mock.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Class<?> type = parameterContext.getParameter().getType();
		Store mockStore = extensionContext.getStore(ExtensionContext.Namespace.create(MockitoExtension.class, type));
		return getMockName(parameterContext.getParameter())
				.map(name -> mockStore.getOrComputeIfAbsent(name, key -> mock(type, key)))
				.orElseGet(() -> mockStore.getOrComputeIfAbsent(type.getCanonicalName(), key -> mock(type)));

	}

	@Override
	public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
		initMocks(testInstance);
	}


	private Optional<String> getMockName(Parameter methodParameter) {
		Mock mockDefinition = methodParameter.getAnnotation(Mock.class);
		Optional<String> mockName = Optional.empty();

		if (isNotBlank(mockDefinition.name())) {
			mockName = Optional.of(trim(mockDefinition.name()));
		}

		if (!mockName.isPresent() && methodParameter.isNamePresent()) {
			mockName = Optional.of(methodParameter.getName());
		}

		return mockName;

	}
}

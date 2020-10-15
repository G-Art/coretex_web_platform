package com.coretex.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ApplicationConfig implements EnvironmentAware {

	private final Map<String, Object> properties = new HashMap<>();

	@Resource
	private ConversionService conversionService;

	private Environment environment;

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

	public Map<String, Object> getConfigurations() {
		if (properties.isEmpty()) {
			properties.putAll(getAllKnownProperties(environment));
		}
		return properties;
	}

	public <R> Optional<R> getConfig(String key, Class<R> type) {
		if (getConfigurations().containsKey(key)) {
			return Optional.of(conversionService.convert(getConfigurations().get(key), type));
		} else {
			return Optional.empty();
		}
	}

	public <R> Optional<R> getConfig(String key, Class<R> type, R defaultValue) {
		return Optional.of(conversionService.convert(getConfigurations().getOrDefault(key, defaultValue),type));
	}

	public Map<String, Object> getAllKnownProperties(Environment env) {
		Map<String, Object> rtn = new HashMap<>();
		if (env instanceof ConfigurableEnvironment) {
			for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
				if (propertySource instanceof EnumerablePropertySource) {
					for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
						var value = propertySource.getProperty(key);
						if (LOG.isDebugEnabled()) {
							LOG.debug(String.format("Environment property key::[%s], value::[%s]", key, value));
						}
						rtn.put(key, value);
					}
				}
			}
		}
		return rtn;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}

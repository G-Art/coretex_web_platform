package com.coretex.searchengine.solr.server.impl;

import com.coretex.core.utils.PlatformUtils;
import com.coretex.searchengine.solr.server.SolrServerConfigurationProvider;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DefaultSolrServerConfigurationProvider implements SolrServerConfigurationProvider, EnvironmentAware {

	private final Map<String, String> properties = new HashMap<>();
	private Environment environment;

	public DefaultSolrServerConfigurationProvider() {
	}

	public Map<String, String> getConfiguration() {
		if (properties.isEmpty()) {
			properties.putAll(getAllKnownProperties(environment));
		}
		String extDir = PlatformUtils.getModulePath("solrserver").getAbsolutePath();
		Path solrServerPath = Paths.get(extDir, "resources", "server");
		properties.put("SOLR_SERVER_PATH", solrServerPath.toString());
		return properties;
	}

	public Map<String, String> getAllKnownProperties(Environment env) {
		Map<String, String> rtn = new HashMap<>();
		if (env instanceof ConfigurableEnvironment) {
			for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
				if (propertySource instanceof EnumerablePropertySource) {
					for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
						rtn.put(key, String.valueOf(propertySource.getProperty(key)));
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

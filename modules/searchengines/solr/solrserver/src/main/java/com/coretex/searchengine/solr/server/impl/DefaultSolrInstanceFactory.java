package com.coretex.searchengine.solr.server.impl;

import com.coretex.searchengine.solr.exceptions.SolrServerException;
import com.coretex.searchengine.solr.server.SolrInstance;
import com.coretex.searchengine.solr.server.SolrInstanceFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultSolrInstanceFactory implements SolrInstanceFactory {
	protected static final String SOLR_INSTANCES_PATH = "/solr/instances";

	public DefaultSolrInstanceFactory() {
	}

	public SolrInstance getInstanceForName(Map<String, String> configuration, String name) throws SolrServerException {
		Map<String, SolrInstance> instances = this.createInstances(configuration);
		SolrInstance instance = instances.get(name);
		if (instance == null) {
			throw new SolrServerException(MessageFormat.format("Solr instance not found for name ''{0}''", name));
		} else {
			return instance;
		}
	}

	public Collection<SolrInstance> getInstances(Map<String, String> configuration) throws SolrServerException {
		Map<String, SolrInstance> instances = this.createInstances(configuration);
		return instances.values();
	}

	protected Map<String, SolrInstance> createInstances(Map<String, String> configuration) {
		Map<String, SolrInstance> instances = new HashMap<>();
		Map<String, String> instancesConfiguration = this.buildInstancesConfiguration(configuration);
		instancesConfiguration.forEach((key, value) -> {
			int instanceNameEndIndex = key.indexOf(46);
			String instanceName = key.substring(0, instanceNameEndIndex);
			String instanceConfigKey = key.substring(instanceNameEndIndex + 1);
			SolrInstance instance = instances.get(instanceName);
			if (instance == null) {
				instance = this.initializeInstance(instanceName, configuration);
				instances.put(instanceName, instance);
			}

			instance.getConfiguration().put(instanceConfigKey, value);
		});
		return instances;
	}

	protected Map<String, String> buildInstancesConfiguration(Map<String, String> configuration) {
		Map<String, String> instancesConfiguration = extractConfiguration(configuration, "solrserver.instances\\.(.*)");
		String instanceName = configuration.get("instance.name");
		if (instanceName != null) {
			Map<String, String> instanceConfiguration = extractConfiguration(configuration, "instance\\.(.*)");
			instanceConfiguration.forEach((key, value) -> {
				instancesConfiguration.put(instanceName + "." + key, value);
			});
		}

		return instancesConfiguration;
	}

	protected static Map<String, String> extractConfiguration(Map<String, String> configuration, String regex) {
		Map<String, String> newConfiguration = new HashMap<>();
		Pattern pattern = Pattern.compile(regex, 2);

		for (Map.Entry<String, String> stringStringEntry : configuration.entrySet()) {
			Matcher matcher = pattern.matcher(stringStringEntry.getKey());
			if (matcher.matches()) {
				newConfiguration.put(matcher.group(1), stringStringEntry.getValue());
			}
		}

		return newConfiguration;
	}

	protected SolrInstance initializeInstance(String name, Map<String, String> configuration) {
		SolrInstance instance = new DefaultSolrInstance(name);
		Map<String, String> instanceConfiguration = instance.getConfiguration();
		Path configPath = Paths.get(configuration.get("coretex.config.path"), SOLR_INSTANCES_PATH, name);
		Path dataPath = Paths.get(configuration.get("coretex.data"), SOLR_INSTANCES_PATH, name);
		Path logPath = Paths.get(configuration.get("catalina.home"), "/logs" + SOLR_INSTANCES_PATH, name);
		Path sslStorePath = configPath.resolve("solr.jks");
		instanceConfiguration.put("config.dir", configPath.toString());
		instanceConfiguration.put("data.dir", dataPath.toString());
		instanceConfiguration.put("log.dir", logPath.toString());
		instanceConfiguration.put("ssl.keyStore", sslStorePath.toString());
		instanceConfiguration.put("ssl.trustStore", sslStorePath.toString());
		return instance;
	}
}

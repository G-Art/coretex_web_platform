package com.coretex.searchengine.solr.server.impl;

import com.coretex.core.shell.CommandBuilder;
import com.coretex.searchengine.solr.server.SolrInstance;
import com.coretex.searchengine.solr.server.SolrServerMode;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolrCommonParamsCommandBuilder implements CommandBuilder {
	protected static final String ZK_DATA_PATH = "zoo_data";
	protected static final String BASIC_AUTH_TYPE = "basic";
	private final Map<String, String> configuration;
	private final SolrInstance solrInstance;

	public SolrCommonParamsCommandBuilder(Map<String, String> configuration, SolrInstance solrInstance) {
		this.configuration = configuration;
		this.solrInstance = solrInstance;
	}

	public Map<String, String> getConfiguration() {
		return this.configuration;
	}

	public SolrInstance getSolrInstance() {
		return this.solrInstance;
	}

	public void build(ProcessBuilder processBuilder) {
		List<String> commandParams = new ArrayList();
		if (SolrServerMode.CLOUD.equals(this.solrInstance.getMode())) {
			commandParams.add("-c");

			commandParams.add("-DzkServerDataDir=" + Paths.get(this.solrInstance.getDataDir(), ZK_DATA_PATH).toString());
		}

		if (StringUtils.isNotBlank(this.solrInstance.getHostName())) {
			commandParams.add("-h");
			commandParams.add(this.solrInstance.getHostName());
		}

		commandParams.add("-force");
		commandParams.add("-p");
		commandParams.add(Integer.toString(this.solrInstance.getPort()));
		if (StringUtils.isNotBlank(this.solrInstance.getMemory())) {
			commandParams.add("-m");
			commandParams.add(this.solrInstance.getMemory());
		}

		if (StringUtils.isNotBlank(this.solrInstance.getJavaOptions())) {
			commandParams.add("-a");
			commandParams.add(this.solrInstance.getJavaOptions());
		}

		processBuilder.command().addAll(commandParams);
		String authType = this.solrInstance.getAuthType();
		String user = this.solrInstance.getUser();
		String password = this.solrInstance.getPassword();
		if (BASIC_AUTH_TYPE.equalsIgnoreCase(authType) && StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
			processBuilder.environment().put("SOLR_AUTH_TYPE", authType);
			processBuilder.environment().put("SOLR_AUTHENTICATION_OPTS", "-Dbasicauth=" + user + ":" + password);
		} else {
			processBuilder.environment().put("SOLR_AUTH_TYPE", "");
			processBuilder.environment().put("SOLR_AUTHENTICATION_OPTS", "");
		}

		boolean sslEnabled = this.solrInstance.isSSLEnabled();
		if (sslEnabled) {
			processBuilder.environment().put("SOLR_SSL_ENABLED", Boolean.TRUE.toString());
			processBuilder.environment().put("SOLR_SSL_KEY_STORE_TYPE", this.solrInstance.getSSLKeyStoreType());
			processBuilder.environment().put("SOLR_SSL_KEY_STORE", this.solrInstance.getSSLKeyStore());
			processBuilder.environment().put("SOLR_SSL_KEY_STORE_PASSWORD", this.solrInstance.getSSLKeyStorePassword());
			processBuilder.environment().put("SOLR_SSL_TRUST_STORE_TYPE", this.solrInstance.getSSLTrustStoreType());
			processBuilder.environment().put("SOLR_SSL_TRUST_STORE", this.solrInstance.getSSLTrustStore());
			processBuilder.environment().put("SOLR_SSL_TRUST_STORE_PASSWORD", this.solrInstance.getSSLTrustStorePassword());
			processBuilder.environment().put("SOLR_SSL_NEED_CLIENT_AUTH", Boolean.toString(this.solrInstance.isSSLNeedClientAuth()));
			processBuilder.environment().put("SOLR_SSL_WANT_CLIENT_AUTH", Boolean.toString(this.solrInstance.isSSLWantClientAuth()));
		} else {
			processBuilder.environment().put("SOLR_SSL_ENABLED", Boolean.FALSE.toString());
		}

	}
}
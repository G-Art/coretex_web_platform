package com.coretex.searchengine.solr.server.impl;

import com.coretex.searchengine.solr.server.SolrInstance;
import com.coretex.searchengine.solr.server.SolrServerMode;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultSolrInstance implements SolrInstance {

	public static final String AUTOSTART_PROPERTY = "autostart";
	public static final String AUTOSTART_DEFAULT_VALUE;
	public static final String PRIORITY_PROPERTY = "priority";
	public static final String PRIORITY_DEFAULT_VALUE = "100";
	public static final String HOST_NAME_PROPERTY = "hostname";
	public static final String HOST_NAME_DEFAULT_VALUE = "localhost";
	public static final String PORT_PROPERTY = "port";
	public static final String PORT_DEFAULT_VALUE = "8983";
	public static final String MODE_PROPERTY = "mode";
	public static final String MODE_DEFAULT_VALUE = "standalone";
	public static final String CONFIG_DIR_PROPERTY = "config.dir";
	public static final String CONFIG_DIR_DEFAULT_VALUE = "";
	public static final String DATA_DIR_PROPERTY = "data.dir";
	public static final String DATA_DIR_DEFAULT_VALUE = "";
	public static final String LOG_DIR_PROPERTY = "log.dir";
	public static final String LOG_DIR_DEFAULT_VALUE = "";
	public static final String MEMORY_PROPERTY = "memory";
	public static final String MEMORY_DEFAULT_VALUE = "512m";
	public static final String JAVA_OPTIONS_PROPERTY = "javaoptions";
	public static final String JAVA_OPTIONS_DEFAULT_VALUE = "";
	public static final String AUTH_TYPE_PROPERTY = "authtype";
	public static final String AUTH_TYPE_DEFAULT_VALUE = "basic";
	public static final String USER_PROPERTY = "user";
	public static final String USER_DEFAULT_VALUE = "";
	public static final String PASSWORD_PROPERTY = "password";
	public static final String PASSWORD_DEFAULT_VALUE = "";
	public static final String SSL_ENABLED = "ssl.enabled";
	public static final String SSL_ENABLED_DEFAULT_VALUE;
	public static final String SSL_KEY_STORE_TYPE = "ssl.keyStoreType";
	public static final String SSL_KEY_STORE_TYPE_DEFAULT_VALUE = "JKS";
	public static final String SSL_KEY_STORE = "ssl.keyStore";
	public static final String SSL_KEY_STORE_DEFAULT_VALUE;
	public static final String SSL_KEY_STORE_PASSWORD = "ssl.keyStorePassword";
	public static final String SSL_KEY_STORE_PASSWORD_DEFAULT_VALUE = "";
	public static final String SSL_TRUST_STORE_TYPE = "ssl.trustStoreType";
	public static final String SSL_TRUST_STORE_TYPE_DEFAULT_VALUE = "JKS";
	public static final String SSL_TRUST_STORE = "ssl.trustStore";
	public static final String SSL_TRUST_STORE_DEFAULT_VALUE;
	public static final String SSL_TRUST_STORE_PASSWORD = "ssl.trustStorePassword";
	public static final String SSL_TRUST_STORE_PASSWORD_DEFAULT_VALUE = "";
	public static final String SSL_NEED_CLIENT_AUTH = "ssl.needClientAuth";
	public static final String SSL_NEED_CLIENT_AUTH_DEFAULT_VALUE;
	public static final String SSL_WANT_CLIENT_AUTH = "ssl.wantClientAuth";
	public static final String SSL_WANT_CLIENT_AUTH_DEFAULT_VALUE;
	private final String name;
	private final Map<String, String> configuration;

	public DefaultSolrInstance(String name) {
		this.name = name;
		this.configuration = this.initializeConfiguration();
	}

	public DefaultSolrInstance(String name, Map<String, String> configuration) {
		this.name = name;
		this.configuration = this.initializeConfiguration();
		if (configuration != null && !configuration.isEmpty()) {
			this.configuration.putAll(configuration);
		}

	}

	public String getName() {
		return this.name;
	}

	public Map<String, String> getConfiguration() {
		return this.configuration;
	}

	public boolean isAutostart() {
		return Boolean.parseBoolean(this.configuration.get(AUTOSTART_PROPERTY));
	}

	public int getPriority() {
		return Integer.parseInt(this.configuration.get(PRIORITY_PROPERTY));
	}

	public String getHostName() {
		return this.configuration.get(HOST_NAME_PROPERTY);
	}

	public int getPort() {
		return Integer.parseInt(this.configuration.get(PORT_PROPERTY));
	}

	public SolrServerMode getMode() {
		return SolrServerMode.valueOf(this.configuration.get(MODE_PROPERTY).toUpperCase(Locale.ROOT));
	}

	public String getConfigDir() {
		return this.configuration.get(CONFIG_DIR_PROPERTY);
	}

	public String getDataDir() {
		return this.configuration.get(DATA_DIR_PROPERTY);
	}

	public String getLogDir() {
		return this.configuration.get(LOG_DIR_PROPERTY);
	}

	public String getMemory() {
		return this.configuration.get(MEMORY_PROPERTY);
	}

	public String getJavaOptions() {
		return this.configuration.get(JAVA_OPTIONS_PROPERTY);
	}

	public String getAuthType() {
		return this.configuration.get(AUTH_TYPE_PROPERTY);
	}

	public String getUser() {
		return this.configuration.get(USER_PROPERTY);
	}

	public String getPassword() {
		return this.configuration.get(PASSWORD_PROPERTY);
	}

	public boolean isSSLEnabled() {
		return Boolean.parseBoolean(this.configuration.get(SSL_ENABLED));
	}

	public String getSSLKeyStoreType() {
		return this.configuration.get(SSL_KEY_STORE_TYPE);
	}

	public String getSSLKeyStore() {
		return this.configuration.get(SSL_KEY_STORE);
	}

	public String getSSLKeyStorePassword() {
		return this.configuration.get(SSL_KEY_STORE_PASSWORD);
	}

	public String getSSLTrustStoreType() {
		return this.configuration.get(SSL_TRUST_STORE_TYPE);
	}

	public String getSSLTrustStore() {
		return this.configuration.get(SSL_TRUST_STORE);
	}

	public String getSSLTrustStorePassword() {
		return this.configuration.get(SSL_TRUST_STORE_PASSWORD);
	}

	public boolean isSSLNeedClientAuth() {
		return Boolean.parseBoolean(this.configuration.get(SSL_NEED_CLIENT_AUTH));
	}

	public boolean isSSLWantClientAuth() {
		return Boolean.parseBoolean(this.configuration.get(SSL_WANT_CLIENT_AUTH));
	}

	protected final Map<String, String> initializeConfiguration() {
		Map<String, String> defaultConfig = new HashMap();
		defaultConfig.put(AUTOSTART_PROPERTY, AUTOSTART_DEFAULT_VALUE);
		defaultConfig.put(PRIORITY_PROPERTY, PRIORITY_DEFAULT_VALUE);
		defaultConfig.put(HOST_NAME_PROPERTY, HOST_NAME_DEFAULT_VALUE);
		defaultConfig.put(PORT_PROPERTY, PORT_DEFAULT_VALUE);
		defaultConfig.put(MODE_PROPERTY, MODE_DEFAULT_VALUE);
		defaultConfig.put(CONFIG_DIR_PROPERTY, CONFIG_DIR_DEFAULT_VALUE);
		defaultConfig.put(DATA_DIR_PROPERTY, DATA_DIR_DEFAULT_VALUE);
		defaultConfig.put(LOG_DIR_PROPERTY, LOG_DIR_DEFAULT_VALUE);
		defaultConfig.put(MEMORY_PROPERTY, MEMORY_DEFAULT_VALUE);
		defaultConfig.put(JAVA_OPTIONS_PROPERTY, JAVA_OPTIONS_DEFAULT_VALUE);
		defaultConfig.put(AUTH_TYPE_PROPERTY, AUTH_TYPE_DEFAULT_VALUE);
		defaultConfig.put(USER_PROPERTY, USER_DEFAULT_VALUE);
		defaultConfig.put(PASSWORD_PROPERTY, PASSWORD_DEFAULT_VALUE);
		defaultConfig.put(SSL_ENABLED, SSL_ENABLED_DEFAULT_VALUE);
		defaultConfig.put(SSL_KEY_STORE_TYPE, SSL_KEY_STORE_TYPE_DEFAULT_VALUE);
		defaultConfig.put(SSL_KEY_STORE, SSL_KEY_STORE_DEFAULT_VALUE);
		defaultConfig.put(SSL_KEY_STORE_PASSWORD, SSL_KEY_STORE_PASSWORD_DEFAULT_VALUE);
		defaultConfig.put(SSL_TRUST_STORE_TYPE, SSL_TRUST_STORE_TYPE_DEFAULT_VALUE);
		defaultConfig.put(SSL_TRUST_STORE, SSL_TRUST_STORE_DEFAULT_VALUE);
		defaultConfig.put(SSL_TRUST_STORE_PASSWORD, SSL_TRUST_STORE_PASSWORD_DEFAULT_VALUE);
		defaultConfig.put(SSL_NEED_CLIENT_AUTH, SSL_NEED_CLIENT_AUTH_DEFAULT_VALUE);
		defaultConfig.put(SSL_WANT_CLIENT_AUTH, SSL_WANT_CLIENT_AUTH_DEFAULT_VALUE);
		return defaultConfig;
	}

	public String toString() {
		return MessageFormat.format("[name: {0}, hostname: {1}, port: {2,number,#}, mode: {3}]", this.getName(), this.getHostName(), this.getPort(), this.getMode());
	}

	static {
		AUTOSTART_DEFAULT_VALUE = Boolean.FALSE.toString();
		SSL_ENABLED_DEFAULT_VALUE = Boolean.TRUE.toString();
		SSL_KEY_STORE_DEFAULT_VALUE = null;
		SSL_TRUST_STORE_DEFAULT_VALUE = null;
		SSL_NEED_CLIENT_AUTH_DEFAULT_VALUE = Boolean.FALSE.toString();
		SSL_WANT_CLIENT_AUTH_DEFAULT_VALUE = Boolean.FALSE.toString();
	}
}

package com.coretex.search.utils;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton
 *
 * @author Carl Samson
 * <p>
 * Now using Jest elasticsearch client to query through http interface
 * <p>
 * https://github.com/searchbox-io/Jest/tree/master/jest
 */
public class SearchClient {

	private static Logger log = LoggerFactory.getLogger(SearchClient.class);

	private JestClient client = null;
	private Node node = null;
	private boolean init = false;
	private ServerConfiguration serverConfiguration = null;

	private String authenticationHeader = null;

	public ServerConfiguration getServerConfiguration() {
		return serverConfiguration;
	}

	public void setServerConfiguration(ServerConfiguration serverConfiguration) {
		this.serverConfiguration = serverConfiguration;
	}

	public JestClient getClient() {
		if (!init) {
			initClient();
		}
		return client;
	}

	public SearchClient() {
	}

	private synchronized void initClient() {

		if (client == null) {

			try {

				// host
				// port
				// proxy settings
				// security

				StringBuilder host = new StringBuilder().append(getServerConfiguration().getClusterHost())
						.append(":").append(getServerConfiguration().getClusterPort());

				JestClientFactory factory = new JestClientFactory();

				HttpClientConfig.Builder httpConfigBuilder = new HttpClientConfig
						.Builder(host.toString()).multiThreaded(true);

				if (getServerConfiguration().getSecurityEnabled() != null && getServerConfiguration().getSecurityEnabled().booleanValue()) {
					httpConfigBuilder.defaultCredentials(getServerConfiguration().getElasticSearchUser(), getServerConfiguration().getElasticSearchPassword());
				}

				factory.setHttpClientConfig(httpConfigBuilder.build());

				client = factory.getObject();

				if (!StringUtils.isBlank(getServerConfiguration().getProxyUser())
						&& !StringUtils.isBlank(getServerConfiguration().getProxyPassword())) {
					setAuthenticationHeader("Basic " + new String(
							Base64.encodeBase64(String.format("%s:%s", getServerConfiguration().getProxyUser(),
									getServerConfiguration().getProxyPassword()).getBytes())));

				}

				log.debug("****** ES client ready ********");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public String getAuthenticationHeader() {
		return authenticationHeader;
	}

	public void setAuthenticationHeader(String authenticationHeader) {
		this.authenticationHeader = authenticationHeader;
	}

}

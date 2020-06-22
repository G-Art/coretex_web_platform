package com.coretex.searchengine.solr.client.providers.impl;

import com.coretex.searchengine.solr.client.providers.SolrClientProvider;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Http2SolrClientProvider implements SolrClientProvider<Http2SolrClient> {

	@Value("${solr.conf.masterUrl}")
	private String solrUrl;
	@Value("${solr.conf.connectionTimeout:10000}")
	private Integer connectionTimeout = 10000;
	@Value("${solr.conf.idleTimeout:60000}")
	private Integer idleTimeout = 60000;

	@Override
	public String clientType() {
		return Http2SolrClient.class.getSimpleName();
	}

	@Override
	public Http2SolrClient creteSolrClient() {
		var builder = new Http2SolrClient.Builder(solrUrl);
		builder.connectionTimeout(connectionTimeout);
		builder.idleTimeout(idleTimeout);
		return builder.build();
	}
}

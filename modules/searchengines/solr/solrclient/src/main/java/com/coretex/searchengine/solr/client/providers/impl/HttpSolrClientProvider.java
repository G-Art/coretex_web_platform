package com.coretex.searchengine.solr.client.providers.impl;

import com.coretex.searchengine.solr.client.providers.SolrClientProvider;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpSolrClientProvider implements SolrClientProvider<HttpSolrClient> {

	@Value("${solr.conf.masterUrl}")
	private String solrUrl;
	@Value("${solr.conf.connectionTimeout:10000}")
	private Integer connectionTimeout = 10000;
	@Value("${solr.conf.idleTimeout:60000}")
	private Integer idleTimeout = 60000;

	@Override
	public String clientType() {
		return HttpSolrClient.class.getSimpleName();
	}

	@Override
	public HttpSolrClient creteSolrClient() {
		var builder = new HttpSolrClient.Builder(solrUrl);
		builder.withConnectionTimeout(connectionTimeout);
		builder.withSocketTimeout(idleTimeout);
		return builder.build();
	}
}

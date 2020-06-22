package com.coretex.searchengine.solr.client.providers;

import org.apache.solr.client.solrj.SolrClient;

public interface SolrClientProvider<T extends SolrClient> {

	String clientType();

	T creteSolrClient();
}

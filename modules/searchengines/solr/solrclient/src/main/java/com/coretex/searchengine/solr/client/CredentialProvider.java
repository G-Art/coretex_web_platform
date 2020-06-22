package com.coretex.searchengine.solr.client;

import org.apache.solr.client.solrj.SolrRequest;

public interface CredentialProvider {


	void set(SolrRequest<?> solrRequest);
}

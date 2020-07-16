package com.coretex.searchengine.solr.client;

import com.coretex.searchengine.solr.client.providers.SolrQueryConfigurationProvider;
import com.coretex.searchengine.solr.client.search.SolrSearchResponse;
import org.apache.solr.client.solrj.response.QueryResponse;

public interface SolrResponseDataConverter {

	<R> void convert(QueryResponse qr, SolrSearchResponse<R> response, SolrQueryConfigurationProvider solrQueryConfigurationProvider);
}

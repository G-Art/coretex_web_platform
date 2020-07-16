package com.coretex.searchengine.solr.client;

import com.coretex.searchengine.solr.client.search.SolrSearchRequest;
import com.coretex.searchengine.solr.client.search.SolrSearchResponse;
import org.apache.solr.client.solrj.response.QueryResponse;

public interface SolrResponseConverter {

	<R> SolrSearchResponse<R> convert(QueryResponse qr, SolrSearchRequest<?> solrSearchRequest);
}

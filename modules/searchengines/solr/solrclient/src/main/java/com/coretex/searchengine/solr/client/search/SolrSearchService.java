package com.coretex.searchengine.solr.client.search;

public interface SolrSearchService {

	<R> SolrSearchResponse<R> search(SolrSearchRequest<?> solrSearchRequest);

}

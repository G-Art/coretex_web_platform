package com.coretex.searchengine.solr.client.search.impl;

import com.coretex.searchengine.solr.client.SolrClientService;
import com.coretex.searchengine.solr.client.SolrResponseConverter;
import com.coretex.searchengine.solr.client.builders.SolrQueryBuilder;
import com.coretex.searchengine.solr.client.search.SolrSearchRequest;
import com.coretex.searchengine.solr.client.search.SolrSearchResponse;
import com.coretex.searchengine.solr.client.search.SolrSearchService;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DefaultSolrSearchService implements SolrSearchService {

	@Resource
	private SolrClientService solrClientService;

	@Resource
	private SolrResponseConverter solrResponseConverter;

	@Override
	public <R> SolrSearchResponse<R> search(SolrSearchRequest<?> solrSearchRequest) {

		var solrQuery = getSolrQueryBuilder()
				.setIncludeScore(true)
				.setSolrSearchRequest(solrSearchRequest)
				.build();

		QueryResponse solrResponse = solrClientService.query(solrQuery);

		return solrResponseConverter.convert(solrResponse, solrSearchRequest);
	}


	@Lookup()
	public SolrQueryBuilder getSolrQueryBuilder(){
		throw new UnsupportedOperationException("Lookup method is not implemented");
	}
}

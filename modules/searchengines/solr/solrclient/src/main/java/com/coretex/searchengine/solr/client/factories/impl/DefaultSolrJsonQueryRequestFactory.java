package com.coretex.searchengine.solr.client.factories.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;

public class DefaultSolrJsonQueryRequestFactory extends AbstractSolrRequestFactory<SolrQuery, QueryRequest>{

	@Override
	protected QueryRequest createType(SolrQuery param) {
		return new JsonQueryRequest(param);
	}
}

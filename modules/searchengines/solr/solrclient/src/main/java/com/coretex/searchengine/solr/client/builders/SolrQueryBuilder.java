package com.coretex.searchengine.solr.client.builders;

import com.coretex.searchengine.solr.client.builders.impl.DefaultSolrQueryBuilder;
import com.coretex.searchengine.solr.client.search.SolrSearchRequest;
import org.apache.solr.client.solrj.SolrQuery;

public interface SolrQueryBuilder {

	DefaultSolrQueryBuilder setIncludeScore(boolean includeScore);

	DefaultSolrQueryBuilder setQueryType(DefaultSolrQueryBuilder.QueryType queryType);

	DefaultSolrQueryBuilder setSolrSearchRequest(SolrSearchRequest<?> solrSearchRequest);

	SolrQuery build();
}

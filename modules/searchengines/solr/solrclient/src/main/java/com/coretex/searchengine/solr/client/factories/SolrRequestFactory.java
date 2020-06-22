package com.coretex.searchengine.solr.client.factories;

import org.apache.solr.client.solrj.SolrRequest;

public interface SolrRequestFactory<P, T extends SolrRequest<?>> {

	 T create(P param) ;
}

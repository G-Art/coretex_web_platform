package com.coretex.searchengine.solr.client;

import com.coretex.searchengine.solr.client.builders.SolrInputDocumentBuilder;

public interface TypeSolrDocumentBuildersRegistry {

	<T> SolrInputDocumentBuilder<T> findBuilderForClass(Class<T> aClass);
}

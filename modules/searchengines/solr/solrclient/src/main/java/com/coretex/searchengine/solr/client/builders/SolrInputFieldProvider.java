package com.coretex.searchengine.solr.client.builders;

import org.apache.solr.common.SolrInputDocument;

public interface SolrInputFieldProvider<S> {



	void setSolrInputField(SolrInputDocument document, S source);

	String fieldName();
}

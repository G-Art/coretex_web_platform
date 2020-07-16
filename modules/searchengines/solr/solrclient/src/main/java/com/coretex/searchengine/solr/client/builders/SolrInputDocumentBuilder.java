package com.coretex.searchengine.solr.client.builders;

import org.apache.solr.common.SolrInputDocument;

public interface SolrInputDocumentBuilder<S> {

	SolrInputDocument build(S source);

	Class<S> getSourceType();
}

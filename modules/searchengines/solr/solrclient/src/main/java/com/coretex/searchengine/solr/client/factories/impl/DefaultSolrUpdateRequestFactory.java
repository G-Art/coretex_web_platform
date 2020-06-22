package com.coretex.searchengine.solr.client.factories.impl;

import org.apache.solr.client.solrj.request.UpdateRequest;

public class DefaultSolrUpdateRequestFactory extends AbstractSolrRequestFactory<Void, UpdateRequest> {

	@Override
	protected UpdateRequest createType(Void ignore) {
		return new UpdateRequest();
	}
}

package com.coretex.searchengine.solr.client.builders.impl;

import com.coretex.items.core.GenericItem;

public class GenericItemSolrInputDocumentBuilder<S extends GenericItem> extends AbstractSolrInputDocumentBuilder<S> {

	public GenericItemSolrInputDocumentBuilder(Class<S> sourceType) {
		super(sourceType);
	}
}

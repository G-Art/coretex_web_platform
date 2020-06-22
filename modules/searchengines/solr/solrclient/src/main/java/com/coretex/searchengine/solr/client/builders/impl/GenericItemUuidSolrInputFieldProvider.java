package com.coretex.searchengine.solr.client.builders.impl;

import com.coretex.items.core.GenericItem;
import com.coretex.searchengine.solr.client.builders.SolrInputFieldProvider;
import org.apache.solr.common.SolrInputDocument;

public class GenericItemUuidSolrInputFieldProvider implements SolrInputFieldProvider<GenericItem> {

	public static final String FIELD_UUID = "uuid";

	@Override
	public void setSolrInputField(SolrInputDocument document, GenericItem source) {
		document.addField(FIELD_UUID, source.getUuid().toString());
	}

	@Override
	public String fieldName() {
		return FIELD_UUID;
	}
}

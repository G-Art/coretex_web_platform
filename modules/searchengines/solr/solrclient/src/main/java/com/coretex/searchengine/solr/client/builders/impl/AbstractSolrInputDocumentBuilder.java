package com.coretex.searchengine.solr.client.builders.impl;

import com.coretex.searchengine.solr.client.builders.SolrInputDocumentBuilder;
import com.coretex.searchengine.solr.client.builders.SolrInputFieldProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;

import java.util.List;

public abstract class AbstractSolrInputDocumentBuilder<S> implements SolrInputDocumentBuilder<S> {

	private List<SolrInputFieldProvider<S>> solrInputFieldProviders;

	private final Class<S> sourceType;

	public AbstractSolrInputDocumentBuilder(Class<S> sourceType) {
		this.sourceType = sourceType;
	}

	@Override
	public SolrInputDocument build(S source) {
		SolrInputDocument solrInput = createDocument();

		if (CollectionUtils.isNotEmpty(solrInputFieldProviders)) {
			solrInputFieldProviders.forEach(provider -> provider.setSolrInputField(solrInput, source));
		}

		return solrInput;
	}

	protected SolrInputDocument createDocument() {
		return new SolrInputDocument();
	}

	public void setSolrInputFieldProviders(List<SolrInputFieldProvider<S>> solrInputFieldProviders) {
		this.solrInputFieldProviders = solrInputFieldProviders;
	}

	@Override
	public Class<S> getSourceType() {
		return sourceType;
	}
}

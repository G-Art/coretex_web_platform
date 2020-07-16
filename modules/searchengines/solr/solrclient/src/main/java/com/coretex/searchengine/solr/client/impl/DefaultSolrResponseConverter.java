package com.coretex.searchengine.solr.client.impl;

import com.coretex.searchengine.solr.client.SolrResponseConverter;
import com.coretex.searchengine.solr.client.providers.SolrQueryConfigurationProvider;
import com.coretex.searchengine.solr.client.search.SolrSearchRequest;
import com.coretex.searchengine.solr.client.search.SolrSearchResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Objects.nonNull;

public class DefaultSolrResponseConverter implements SolrResponseConverter {

	private final Logger LOG = LoggerFactory.getLogger(DefaultSolrResponseConverter.class);

	private final Map<Class<?>, SolrQueryConfigurationProvider> configurationProviderMap;


	public DefaultSolrResponseConverter(Map<Class<?>, SolrQueryConfigurationProvider> configurationProviderMap) {
		this.configurationProviderMap = configurationProviderMap;
	}

	@Override
	public <R> SolrSearchResponse<R> convert(QueryResponse qr, SolrSearchRequest<?> solrSearchRequest) {
		SolrSearchResponse<R> response = createResponse();
		response.setRequest(solrSearchRequest);
		var solrQueryConfigurationProvider = configurationProviderMap.get(solrSearchRequest.getResultType());
		if (nonNull(solrQueryConfigurationProvider)) {
			if (nonNull(solrQueryConfigurationProvider.getSolrResponseFacetDataConverter())) {
				solrQueryConfigurationProvider.getSolrResponseFacetDataConverter()
						.convert(qr, response, solrQueryConfigurationProvider);
			} else {
				LOG.warn("Facet converter is not set");
			}

			if (nonNull(solrQueryConfigurationProvider.getSolrResponseDataConverter())) {
				solrQueryConfigurationProvider.getSolrResponseDataConverter()
						.convert(qr, response, solrQueryConfigurationProvider);
			} else {
				LOG.warn("Data converter is not set");
			}

		}
		return response;
	}

	protected <R> SolrSearchResponse<R> createResponse() {
		return new SolrSearchResponse<>();
	}
}

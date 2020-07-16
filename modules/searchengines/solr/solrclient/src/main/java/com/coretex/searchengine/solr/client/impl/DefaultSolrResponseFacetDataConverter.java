package com.coretex.searchengine.solr.client.impl;

import com.coretex.searchengine.solr.client.SolrResponseDataConverter;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import com.coretex.searchengine.solr.client.providers.SolrQueryConfigurationProvider;
import com.coretex.searchengine.solr.client.search.FacetData;
import com.coretex.searchengine.solr.client.search.SolrSearchResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.stream.Collectors;

public class DefaultSolrResponseFacetDataConverter implements SolrResponseDataConverter {

	@Override
	public <R> void convert(QueryResponse qr, SolrSearchResponse<R> response, SolrQueryConfigurationProvider solrQueryConfigurationProvider) {
		qr.getFacetFields()
				.forEach(facetField -> {
					var name = StringUtils.substringBefore(facetField.getName(), "_");
					solrQueryConfigurationProvider.solrDocFieldConfigs()
							.stream()
							.filter(solrDocFieldConfig -> solrDocFieldConfig.getName().equals(name))
							.findAny()
							.ifPresent(config -> convertFacet(config, facetField, response));
				});
	}

	private <R> void convertFacet(SolrDocFieldConfig config, FacetField facetField, SolrSearchResponse<R> response) {
		var facetData = new FacetData();
		facetData.setTitle(config.getName());
		facetData.setValues(facetField.getValues()
				.stream()
				.map(count -> new FacetData.FacetValueData(count.getName(), count.getCount()))
				.collect(Collectors.toList()));
		response.add(facetData);
	}
}

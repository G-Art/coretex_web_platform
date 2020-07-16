package com.coretex.searchengine.solr.client.providers;

import com.coretex.searchengine.solr.client.SolrResponseDataConverter;

import java.util.Set;

public interface SolrQueryConfigurationProvider {

	boolean grouped();

	boolean nGrouped();

	int groupLimit();

	Set<SolrDocFieldConfig> solrDocFieldConfigs();

	SolrDocFieldConfig solrDocFieldConfigsByName(String name);

	String groupField();

	boolean groupFacet();
	boolean facet();

	SolrResponseDataConverter getSolrResponseFacetDataConverter();

	SolrResponseDataConverter getSolrResponseDataConverter();
}

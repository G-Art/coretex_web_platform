package com.coretex.searchengine.solr.client.providers.impl;

import com.coretex.searchengine.solr.client.SolrResponseDataConverter;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import com.coretex.searchengine.solr.client.providers.SolrQueryConfigurationProvider;
import com.coretex.searchengine.solr.client.providers.SolrSortConfig;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DefaultSolrQueryConfigurationProvider implements SolrQueryConfigurationProvider {

	private Set<SolrDocFieldConfig> solrDocFieldConfigs;

	private SolrDocFieldConfig groupBySolrDocFieldConfig;
	private Map<String, SolrSortConfig> solrSortConfigs;

	private SolrResponseDataConverter solrResponseFacetDataConverter;
	private SolrResponseDataConverter solrResponseDataConverter;

	private boolean nGrouped = false;
	private boolean facet = false;
	private boolean groupFacet = false;
	private int groupLimit = -1;
	private int facetMinCount = 1;

	public DefaultSolrQueryConfigurationProvider() {
	}

	public DefaultSolrQueryConfigurationProvider(Set<SolrDocFieldConfig> solrDocFieldConfigs) {
		this.solrDocFieldConfigs = solrDocFieldConfigs;
		init();
	}

	@PostConstruct
	private void init(){
		if(CollectionUtils.isNotEmpty(solrDocFieldConfigs)){
			solrDocFieldConfigs.stream()
					.filter(SolrDocFieldConfig::isGroupedBy)
					.findFirst()
					.ifPresent(solrDocFieldConfig -> this.groupBySolrDocFieldConfig = solrDocFieldConfig);

			facet = solrDocFieldConfigs.stream().anyMatch(SolrDocFieldConfig::isFacet);
		}
	}

	@Override
	public boolean grouped() {
		return Objects.nonNull(groupBySolrDocFieldConfig) && groupBySolrDocFieldConfig.isGroupedBy();
	}

	@Override
	public boolean nGrouped() {
		return grouped() && nGrouped;
	}

	@Override
	public int groupLimit() {
		return groupLimit;
	}

	@Override
	public String groupField() {
		return Objects.nonNull(groupBySolrDocFieldConfig) ? groupBySolrDocFieldConfig.createFullFieldName() : "baseProductCode_string";
	}

	@Override
	public Set<SolrDocFieldConfig> solrDocFieldConfigs() {
		return solrDocFieldConfigs;
	}

	@Override
	public SolrDocFieldConfig solrDocFieldConfigsByName(String name) {
		return solrDocFieldConfigs.stream()
				.filter(conf -> conf.getName().equals(name))
				.findAny()
				.orElse(null);
	}

	public void setnGrouped(boolean nGrouped) {
		this.nGrouped = nGrouped;
	}

	public void setGroupLimit(int groupLimit) {
		this.groupLimit = groupLimit;
	}

	@Override
	public boolean groupFacet() {
		return facet() && groupFacet;
	}

	@Override
	public int facetMinCount() {
		return facetMinCount;
	}

	@Override
	public boolean facet() {
		return facet;
	}

	public void setGroupFacet(boolean groupFacet) {
		this.groupFacet = groupFacet;
	}

	public void setFacetMinCount(int facetMinCount) {
		this.facetMinCount = facetMinCount;
	}

	@Override
	public SolrResponseDataConverter getSolrResponseFacetDataConverter() {
		return solrResponseFacetDataConverter;
	}

	public void setSolrResponseFacetDataConverter(SolrResponseDataConverter solrResponseFacetDataConverter) {
		this.solrResponseFacetDataConverter = solrResponseFacetDataConverter;
	}

	@Override
	public SolrResponseDataConverter getSolrResponseDataConverter() {
		return solrResponseDataConverter;
	}

	public void setSolrResponseDataConverter(SolrResponseDataConverter solrResponseDataConverter) {
		this.solrResponseDataConverter = solrResponseDataConverter;
	}

	@Override
	public Map<String, SolrSortConfig> solrSortConfigs() {
		return solrSortConfigs;
	}

	public void setSolrSortConfigs(Map<String, SolrSortConfig> solrSortConfigs) {
		this.solrSortConfigs = solrSortConfigs;
	}
}

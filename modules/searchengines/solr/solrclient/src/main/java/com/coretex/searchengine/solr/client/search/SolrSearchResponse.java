package com.coretex.searchengine.solr.client.search;

import com.google.common.collect.Lists;

import java.util.List;

public class SolrSearchResponse<T> {
	private SolrSearchRequest<?> request;
	private List<T> result  = Lists.newArrayList();
	private final List<FacetData> facets = Lists.newArrayList();
	private Long totalCount;
	private Integer totalPages;

	public SolrSearchRequest<?> getRequest() {
		return request;
	}

	public void setRequest(SolrSearchRequest<?> request) {
		this.request = request;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public List<FacetData> getFacets() {
		return facets;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public boolean add(FacetData facetData) {
		return facets.add(facetData);
	}
}

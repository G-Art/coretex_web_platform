package com.coretex.commerce.data;

import com.coretex.searchengine.solr.client.search.FacetData;

import java.util.List;

public class SearchPageResult {

	private int page;
	private int totalPages;
	private int count;
	private int totalCount;

	private BreadcrumbData[] breadcrumb;

	private List<ShortProductData> products;
	private List<FacetData> facets;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public BreadcrumbData[] getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(BreadcrumbData[] breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public List<ShortProductData> getProducts() {
		return products;
	}

	public void setProducts(List<ShortProductData> products) {
		this.products = products;
	}

	public void setFacets(List<FacetData> facets) {
		this.facets = facets;
	}

	public List<FacetData> getFacets() {
		return facets;
	}
}

package com.coretex.searchengine.solr.client.search;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class SolrSearchRequest<C> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Class<C> resultType;

	private String searchText;
	private Locale locale = Locale.ENGLISH;
	private final MultiValuedMap<String, String> filters = new ArrayListValuedHashMap<>();
	private int page = 0;
	private int count = 12;
	private String sortCode;
	private String sortOrder;

	public SolrSearchRequest(Class<C> resultType) {
		this.resultType = resultType;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Class<C> getResultType() {
		return resultType;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public MultiValuedMap<String, String> getFilters() {
		return filters;
	}

	public boolean putFilter(String key, String value) {
		return filters.put(key, value);
	}

	public boolean putAllFilters(String key, Iterable<? extends String> values) {
		return filters.putAll(key, values);
	}

	public boolean putAllFilters(Map<? extends String, ? extends String> map) {
		return filters.putAll(map);
	}

	public boolean putAllFilters(MultiValuedMap<? extends String, ? extends String> map) {
		return filters.putAll(map);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setSort(String code, String order) {
		this.sortCode = code;
		this.sortOrder = order;
	}

	public String getSortCode() {
		return sortCode;
	}

	public String getSortOrder() {
		return sortOrder;
	}
}
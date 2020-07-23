package com.coretex.searchengine.solr.client.providers;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class SolrSortConfig {
	private List<Pair<SolrDocFieldConfig, String>> sortingFields;
	private boolean includeScore;
	private String scoreOrder;

	public List<Pair<SolrDocFieldConfig, String>> getSortingFields() {
		return sortingFields;
	}

	public void setSortingFields(List<Pair<SolrDocFieldConfig, String>> sortingFields) {
		this.sortingFields = sortingFields;
	}

	public boolean isIncludeScore() {
		return includeScore;
	}

	public void setIncludeScore(boolean includeScore) {
		this.includeScore = includeScore;
	}

	public String getScoreOrder() {
		return scoreOrder;
	}

	public void setScoreOrder(String scoreOrder) {
		this.scoreOrder = scoreOrder;
	}
}

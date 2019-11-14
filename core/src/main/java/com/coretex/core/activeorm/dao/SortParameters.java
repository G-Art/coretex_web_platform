package com.coretex.core.activeorm.dao;


import java.util.HashMap;
import java.util.Map;

public class SortParameters {
	private final Map<String, SortParameters.SortOrder> params = new HashMap<>();

	public SortParameters() {
	}

	public void addSortParameter(String paramName, SortParameters.SortOrder sortOrder) {
		this.params.put(paramName, sortOrder);
	}

	public void removeSortParameter(String paramName) {
		this.params.remove(paramName);
	}

	public Map<String, SortParameters.SortOrder> getSortParameters() {
		return this.params;
	}

	public boolean isEmpty() {
		return this.params.isEmpty();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof SortParameters)) {
			return false;
		} else {
			return (this.params != null || ((SortParameters) obj).getSortParameters() == null) && this.params.equals(((SortParameters) obj).getSortParameters());
		}
	}

	public int hashCode() {
		return this.params.hashCode();
	}

	public static SortParameters singletonAscending(String paramName) {
		SortParameters params = new SortParameters();
		params.addSortParameter(paramName, SortParameters.SortOrder.ASCENDING);
		return params;
	}

	public static SortParameters singletonDescending(String paramName) {
		SortParameters params = new SortParameters();
		params.addSortParameter(paramName, SortParameters.SortOrder.DESCENDING);
		return params;
	}

	public enum SortOrder {
		ASCENDING("asc"),
		DESCENDING("desc");

		private final String abbreviation;

		SortOrder(String abbreviation) {
			this.abbreviation = abbreviation;
		}

		public String toString() {
			return this.abbreviation;
		}
	}
}


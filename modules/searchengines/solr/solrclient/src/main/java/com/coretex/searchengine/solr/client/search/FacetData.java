package com.coretex.searchengine.solr.client.search;

import java.util.List;

public class FacetData {
	private String title;
	private List<FacetValueData> values;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<FacetValueData> getValues() {
		return values;
	}

	public void setValues(List<FacetValueData> values) {
		this.values = values;
	}

	public static class FacetValueData {
		private String name = null;
		private long count = 0;

		public FacetValueData(String name, long count) {
			this.name = name;
			this.count = count;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}
	}
}

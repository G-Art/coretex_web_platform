package com.coretex.shop.model.catalog;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

/**
 * Search product request
 *
 * @author c.samson
 */
public class SearchProductRequest implements Serializable {


	private static final long serialVersionUID = 1L;
	@NotEmpty
	private String query;
	private int count;
	private int start;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}

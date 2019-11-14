package com.coretex.newpost.api.address.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CitiesProperties {

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "FindByString")
	private String findByString;

	@JsonProperty(value = "Page")
	private Integer page = 0;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getFindByString() {
		return findByString;
	}

	public void setFindByString(String findByString) {
		this.findByString = findByString;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}

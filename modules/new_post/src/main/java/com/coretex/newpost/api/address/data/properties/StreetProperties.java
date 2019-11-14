package com.coretex.newpost.api.address.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreetProperties {

	@JsonProperty(value = "CityRef")
	private String reference;

	@JsonProperty(value = "Page")
	private Integer page = 0;


	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}

package com.coretex.newpost.api.address.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettlementsProperties {

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "RegionRef")
	private String regionReference;

	@JsonProperty(value = "FindByString")
	private String findByString;

	@JsonProperty(value = "Warehouse")
	private String warehouse; // 1 or 0

	@JsonProperty(value = "Page")
	private Integer page = 0;


	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getRegionReference() {
		return regionReference;
	}

	public void setRegionReference(String regionReference) {
		this.regionReference = regionReference;
	}

	public String getFindByString() {
		return findByString;
	}

	public void setFindByString(String findByString) {
		this.findByString = findByString;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}

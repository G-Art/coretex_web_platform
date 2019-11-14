package com.coretex.newpost.api.address.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaValue {

	@JsonProperty(value = "Description")
	private String description;

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "AreasCenter")
	private String areasCenter;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getAreasCenter() {
		return areasCenter;
	}

	public void setAreasCenter(String areasCenter) {
		this.areasCenter = areasCenter;
	}
}

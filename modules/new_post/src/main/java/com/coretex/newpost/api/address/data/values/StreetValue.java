package com.coretex.newpost.api.address.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StreetValue {

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "Description")
	private String description;

	@JsonProperty(value = "StreetsTypeRef")
	private String streetsTypeRef;

	@JsonProperty(value = "StreetsType")
	private String streetsType;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStreetsTypeRef() {
		return streetsTypeRef;
	}

	public void setStreetsTypeRef(String streetsTypeRef) {
		this.streetsTypeRef = streetsTypeRef;
	}

	public String getStreetsType() {
		return streetsType;
	}

	public void setStreetsType(String streetsType) {
		this.streetsType = streetsType;
	}
}

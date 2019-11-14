package com.coretex.newpost.api.actions.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PackCalculateProperties {

	@JsonProperty(value = "PackCount")
	private String packCount;

	@JsonProperty(value = "PackRef")
	private String packRef;

	public String getPackCount() {
		return packCount;
	}

	public void setPackCount(String packCount) {
		this.packCount = packCount;
	}

	public String getPackRef() {
		return packRef;
	}

	public void setPackRef(String packRef) {
		this.packRef = packRef;
	}
}

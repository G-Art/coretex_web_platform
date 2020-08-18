package com.coretex.commerce.admin.init.apparel;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"code",
		"name",
		"store",
		"active"
})
public class ApCategory {

	@JsonProperty("code")
	private String code;
	@JsonProperty("name")
	private ApLocalizedField name;
	@JsonProperty("store")
	private String store;
	@JsonProperty("active")
	private Boolean active;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty("name")
	public ApLocalizedField getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(ApLocalizedField name) {
		this.name = name;
	}

	@JsonProperty("store")
	public String getStore() {
		return store;
	}

	@JsonProperty("store")
	public void setStore(String store) {
		this.store = store;
	}

	@JsonProperty("active")
	public Boolean getActive() {
		return active;
	}

	@JsonProperty("active")
	public void setActive(Boolean active) {
		this.active = active;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
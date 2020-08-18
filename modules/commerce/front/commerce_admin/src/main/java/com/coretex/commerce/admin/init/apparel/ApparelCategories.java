package com.coretex.commerce.admin.init.apparel;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"categories",
		"hierarchy"
})
public class ApparelCategories implements Serializable {

	@JsonProperty("categories")
	private List<ApCategory> categories = null;
	@JsonProperty("hierarchy")
	private List<ApHierarchy> hierarchy = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("categories")
	public List<ApCategory> getCategories() {
		return categories;
	}

	@JsonProperty("categories")
	public void setCategories(List<ApCategory> categories) {
		this.categories = categories;
	}

	@JsonProperty("hierarchy")
	public List<ApHierarchy> getHierarchy() {
		return hierarchy;
	}

	@JsonProperty("hierarchy")
	public void setHierarchy(List<ApHierarchy> hierarchy) {
		this.hierarchy = hierarchy;
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
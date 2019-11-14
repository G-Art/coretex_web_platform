package com.coretex.newpost.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestApiData<P> {



	@JsonProperty
	private String apiKey;
	@JsonProperty
	private String modelName;
	@JsonProperty
	private String calledMethod;
	@JsonProperty
	private P methodProperties;

	public RequestApiData(String apiKey, String modelName, String calledMethod, P methodProperties) {
		this.apiKey = apiKey;
		this.modelName = modelName;
		this.calledMethod = calledMethod;
		this.methodProperties = methodProperties;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getModelName() {
		return modelName;
	}

	public String getCalledMethod() {
		return calledMethod;
	}

	public P getMethodProperties() {
		return methodProperties;
	}
}

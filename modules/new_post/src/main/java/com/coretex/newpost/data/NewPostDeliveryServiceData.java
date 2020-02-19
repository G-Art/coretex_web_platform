package com.coretex.newpost.data;

import java.util.List;

public class NewPostDeliveryServiceData {

	private List<NewPostDeliveryTypeData> deliveryTypes;
	private String apiKey;
	private String endpoint;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public List<NewPostDeliveryTypeData> getDeliveryTypes() {
		return deliveryTypes;
	}

	public void setDeliveryTypes(List<NewPostDeliveryTypeData> deliveryTypes) {
		this.deliveryTypes = deliveryTypes;
	}
}

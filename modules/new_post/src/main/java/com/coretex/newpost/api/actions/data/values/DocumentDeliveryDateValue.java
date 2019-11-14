package com.coretex.newpost.api.actions.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentDeliveryDateValue {

	@JsonProperty(value = "DeliveryDate")
	private DeliveryDateValue deliveryDate;

	public DeliveryDateValue getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(DeliveryDateValue deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
}

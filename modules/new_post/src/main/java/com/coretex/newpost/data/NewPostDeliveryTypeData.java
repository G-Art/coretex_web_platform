package com.coretex.newpost.data;

import java.util.Map;

public class NewPostDeliveryTypeData  {

	public String code;
	public Map<String, String> name;
	public Boolean active = false;
	public Boolean payOnDelivery;
	public NewPostDeliveryServiceData deliveryService;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, String> getName() {
		return name;
	}

	public void setName(Map<String, String> name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getPayOnDelivery() {
		return payOnDelivery;
	}

	public void setPayOnDelivery(Boolean payOnDelivery) {
		this.payOnDelivery = payOnDelivery;
	}

	public NewPostDeliveryServiceData getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(NewPostDeliveryServiceData deliveryService) {
		this.deliveryService = deliveryService;
	}
}

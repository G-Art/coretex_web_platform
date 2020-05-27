package com.coretex.commerce.data;

import java.util.Map;
import java.util.UUID;

public class DeliveryTypeData extends GenericItemData {

	private String code;
	private Map<String, String> name;
	private String active;
	private String type;
	private String deliveryServiceCode;
	private UUID deliveryService;
	private Map<String, Object> additionalInfo;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getName() {
		return name;
	}

	public void setName(Map<String, String> name) {
		this.name = name;
	}

	public UUID getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(UUID deliveryService) {
		this.deliveryService = deliveryService;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getDeliveryServiceCode() {
		return deliveryServiceCode;
	}

	public void setDeliveryServiceCode(String deliveryServiceCode) {
		this.deliveryServiceCode = deliveryServiceCode;
	}
}

package com.coretex.commerce.data;

import java.util.Map;

public class PaymentTypeData extends GenericItemData {
	private String code;
	private Map<String, String> name;
	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

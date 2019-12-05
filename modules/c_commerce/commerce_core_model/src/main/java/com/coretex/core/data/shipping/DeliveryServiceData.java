package com.coretex.core.data.shipping;

import com.coretex.core.data.GenericData;
import com.coretex.core.data.orders.Country;

import java.util.Map;
import java.util.Set;

public class DeliveryServiceData extends GenericData {

	private Map<String, String> name;
	private String code;
	private String image;
	private Boolean active = false;
	private Set<Country> countries;

	public Map<String, String> getName() {
		return name;
	}

	public void setName(Map<String, String> name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<Country> getCountries() {
		return countries;
	}

	public void setCountries(Set<Country> countries) {
		this.countries = countries;
	}

}

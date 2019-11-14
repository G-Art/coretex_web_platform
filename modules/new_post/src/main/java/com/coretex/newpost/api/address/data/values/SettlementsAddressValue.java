package com.coretex.newpost.api.address.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementsAddressValue {


	@JsonProperty(value = "Present")
	private String present;

	@JsonProperty(value = "StreetsAvailability")
	private Boolean streetsAvailability;

	@JsonProperty(value = "Warehouses")
	private Integer warehouses;

	@JsonProperty(value = "MainDescription")
	private String mainDescription;

	@JsonProperty(value = "Area")
	private String area;

	@JsonProperty(value = "Region")
	private String region;

	@JsonProperty(value = "SettlementTypeCode")
	private String settlementTypeCode;

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "DeliveryCity")
	private String deliveryCity;

	public Boolean getStreetsAvailability() {
		return streetsAvailability;
	}

	public void setStreetsAvailability(Boolean streetsAvailability) {
		this.streetsAvailability = streetsAvailability;
	}

	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}

	public Integer getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(Integer warehouses) {
		this.warehouses = warehouses;
	}

	public String getMainDescription() {
		return mainDescription;
	}

	public void setMainDescription(String mainDescription) {
		this.mainDescription = mainDescription;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSettlementTypeCode() {
		return settlementTypeCode;
	}

	public void setSettlementTypeCode(String settlementTypeCode) {
		this.settlementTypeCode = settlementTypeCode;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDeliveryCity() {
		return deliveryCity;
	}

	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}
}

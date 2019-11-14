package com.coretex.newpost.api.address.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseValue {
	@JsonProperty(value = "Description")
	private String description;

	@JsonProperty(value = "DescriptionRu")
	private String descriptionRu;

	@JsonProperty(value = "Phone")
	private String phone;

	@JsonProperty(value = "SiteKey")
	private String siteKey;

	@JsonProperty(value = "TypeOfWarehouse")
	private String typeOfWarehouse;

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "Number")
	private Integer number;

	@JsonProperty(value = "CityRef")
	private String cityRef;

	@JsonProperty(value = "CityDescription")
	private String cityDescription;

	@JsonProperty(value = "CityDescriptionRu")
	private String cityDescriptionRu;

	@JsonProperty(value = "Longitude")
	private Double longitude;

	@JsonProperty(value = "Latitude")
	private Double latitude;

	@JsonProperty(value = "PostFinance")
	private Integer postFinance;

	@JsonProperty(value = "BicycleParking")
	private Integer bicycleParking;

	@JsonProperty(value = "POSTerminal")
	private Integer posTerminal;

	@JsonProperty(value = "InternationalShipping")
	private Integer internationalShipping;

	@JsonProperty(value = "TotalMaxWeightAllowed")
	private Integer totalMaxWeightAllowed;

	@JsonProperty(value = "PlaceMaxWeightAllowed")
	private Integer placeMaxWeightAllowed;

	@JsonProperty(value = "Reception")
	private Map<String, String> reception;

	@JsonProperty(value = "Delivery")
	private Map<String, String> delivery;

	@JsonProperty(value = "Schedule")
	private Map<String, String> schedule;


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionRu() {
		return descriptionRu;
	}

	public void setDescriptionRu(String descriptionRu) {
		this.descriptionRu = descriptionRu;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(String siteKey) {
		this.siteKey = siteKey;
	}

	public String getTypeOfWarehouse() {
		return typeOfWarehouse;
	}

	public void setTypeOfWarehouse(String typeOfWarehouse) {
		this.typeOfWarehouse = typeOfWarehouse;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getCityRef() {
		return cityRef;
	}

	public void setCityRef(String cityRef) {
		this.cityRef = cityRef;
	}

	public String getCityDescription() {
		return cityDescription;
	}

	public void setCityDescription(String cityDescription) {
		this.cityDescription = cityDescription;
	}

	public String getCityDescriptionRu() {
		return cityDescriptionRu;
	}

	public void setCityDescriptionRu(String cityDescriptionRu) {
		this.cityDescriptionRu = cityDescriptionRu;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getPostFinance() {
		return postFinance;
	}

	public void setPostFinance(Integer postFinance) {
		this.postFinance = postFinance;
	}

	public Integer getBicycleParking() {
		return bicycleParking;
	}

	public void setBicycleParking(Integer bicycleParking) {
		this.bicycleParking = bicycleParking;
	}

	public Integer getPosTerminal() {
		return posTerminal;
	}

	public void setPosTerminal(Integer posTerminal) {
		this.posTerminal = posTerminal;
	}

	public Integer getInternationalShipping() {
		return internationalShipping;
	}

	public void setInternationalShipping(Integer internationalShipping) {
		this.internationalShipping = internationalShipping;
	}

	public Integer getTotalMaxWeightAllowed() {
		return totalMaxWeightAllowed;
	}

	public void setTotalMaxWeightAllowed(Integer totalMaxWeightAllowed) {
		this.totalMaxWeightAllowed = totalMaxWeightAllowed;
	}

	public Integer getPlaceMaxWeightAllowed() {
		return placeMaxWeightAllowed;
	}

	public void setPlaceMaxWeightAllowed(Integer placeMaxWeightAllowed) {
		this.placeMaxWeightAllowed = placeMaxWeightAllowed;
	}

	public Map<String, String> getReception() {
		return reception;
	}

	public void setReception(Map<String, String> reception) {
		this.reception = reception;
	}

	public Map<String, String> getDelivery() {
		return delivery;
	}

	public void setDelivery(Map<String, String> delivery) {
		this.delivery = delivery;
	}

	public Map<String, String> getSchedule() {
		return schedule;
	}

	public void setSchedule(Map<String, String> schedule) {
		this.schedule = schedule;
	}
}

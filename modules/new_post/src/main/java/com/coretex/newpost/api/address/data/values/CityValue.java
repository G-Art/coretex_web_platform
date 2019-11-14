package com.coretex.newpost.api.address.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityValue {

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "Description")
	private String description;

	@JsonProperty(value = "DescriptionRu")
	private String descriptionRu;

	@JsonProperty(value = "Delivery1")
	private String deliveryDay1;

	@JsonProperty(value = "Delivery2")
	private String deliveryDay2;

	@JsonProperty(value = "Delivery3")
	private String deliveryDay3;

	@JsonProperty(value = "Delivery4")
	private String deliveryDay4;

	@JsonProperty(value = "Delivery5")
	private String deliveryDay5;

	@JsonProperty(value = "Delivery6")
	private String deliveryDay6;

	@JsonProperty(value = "Delivery7")
	private String deliveryDay7;

	@JsonProperty(value = "Area")
	private String area;

	@JsonProperty(value = "SettlementType")
	private String settlementType;

	@JsonProperty(value = "IsBranch")
	private String branch;

	@JsonProperty(value = "PreventEntryNewStreetsUser")
	private String preventEntryNewStreetsUser;

	@JsonProperty(value = "Conglomerates")
	private String conglomerates;

	@JsonProperty(value = "CityID")
	private String cityID;

	@JsonProperty(value = "SettlementTypeDescriptionRu")
	private String settlementTypeDescriptionRu;

	@JsonProperty(value = "SettlementTypeDescription")
	private String settlementTypeDescription;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

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

	public String getDeliveryDay1() {
		return deliveryDay1;
	}

	public void setDeliveryDay1(String deliveryDay1) {
		this.deliveryDay1 = deliveryDay1;
	}

	public String getDeliveryDay2() {
		return deliveryDay2;
	}

	public void setDeliveryDay2(String deliveryDay2) {
		this.deliveryDay2 = deliveryDay2;
	}

	public String getDeliveryDay3() {
		return deliveryDay3;
	}

	public void setDeliveryDay3(String deliveryDay3) {
		this.deliveryDay3 = deliveryDay3;
	}

	public String getDeliveryDay4() {
		return deliveryDay4;
	}

	public void setDeliveryDay4(String deliveryDay4) {
		this.deliveryDay4 = deliveryDay4;
	}

	public String getDeliveryDay5() {
		return deliveryDay5;
	}

	public void setDeliveryDay5(String deliveryDay5) {
		this.deliveryDay5 = deliveryDay5;
	}

	public String getDeliveryDay6() {
		return deliveryDay6;
	}

	public void setDeliveryDay6(String deliveryDay6) {
		this.deliveryDay6 = deliveryDay6;
	}

	public String getDeliveryDay7() {
		return deliveryDay7;
	}

	public void setDeliveryDay7(String deliveryDay7) {
		this.deliveryDay7 = deliveryDay7;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getPreventEntryNewStreetsUser() {
		return preventEntryNewStreetsUser;
	}

	public void setPreventEntryNewStreetsUser(String preventEntryNewStreetsUser) {
		this.preventEntryNewStreetsUser = preventEntryNewStreetsUser;
	}

	public String getConglomerates() {
		return conglomerates;
	}

	public void setConglomerates(String conglomerates) {
		this.conglomerates = conglomerates;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getSettlementTypeDescriptionRu() {
		return settlementTypeDescriptionRu;
	}

	public void setSettlementTypeDescriptionRu(String settlementTypeDescriptionRu) {
		this.settlementTypeDescriptionRu = settlementTypeDescriptionRu;
	}

	public String getSettlementTypeDescription() {
		return settlementTypeDescription;
	}

	public void setSettlementTypeDescription(String settlementTypeDescription) {
		this.settlementTypeDescription = settlementTypeDescription;
	}
}

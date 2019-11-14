package com.coretex.newpost.api.address.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementValue {

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "Warehouse")
	private Integer warehouse;

	@JsonProperty(value = "Conglomerates")
	private String[] conglomerates;

	@JsonProperty(value = "Index1")
	private String index1;

	@JsonProperty(value = "Index2")
	private String index2;

	@JsonProperty(value = "IndexCOATSU1")
	private String indexCoatsu1;

	@JsonProperty(value = "Area")
	private String area;

	@JsonProperty(value = "AreaDescription")
	private String areaDescription;

	@JsonProperty(value = "AreaDescriptionRu")
	private String areaDescriptionRu;

	@JsonProperty(value = "Region")
	private String region;

	@JsonProperty(value = "RegionsDescription")
	private String regionsDescription;

	@JsonProperty(value = "RegionsDescriptionRu")
	private String regionsDescriptionRu;

	@JsonProperty(value = "SettlementTypeDescription")
	private String settlementTypeDescription;

	@JsonProperty(value = "SettlementTypeDescriptionRu")
	private String settlementTypeDescriptionRu;

	@JsonProperty(value = "SettlementType")
	private String settlementType;

	@JsonProperty(value = "Longitude")
	private Double longitude;

	@JsonProperty(value = "Latitude")
	private Double latitude;

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

	public Integer getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Integer warehouse) {
		this.warehouse = warehouse;
	}

	public String[] getConglomerates() {
		return conglomerates;
	}

	public void setConglomerates(String[] conglomerates) {
		this.conglomerates = conglomerates;
	}

	public String getIndex1() {
		return index1;
	}

	public void setIndex1(String index1) {
		this.index1 = index1;
	}

	public String getIndex2() {
		return index2;
	}

	public void setIndex2(String index2) {
		this.index2 = index2;
	}

	public String getIndexCoatsu1() {
		return indexCoatsu1;
	}

	public void setIndexCoatsu1(String indexCoatsu1) {
		this.indexCoatsu1 = indexCoatsu1;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaDescription() {
		return areaDescription;
	}

	public void setAreaDescription(String areaDescription) {
		this.areaDescription = areaDescription;
	}

	public String getAreaDescriptionRu() {
		return areaDescriptionRu;
	}

	public void setAreaDescriptionRu(String areaDescriptionRu) {
		this.areaDescriptionRu = areaDescriptionRu;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegionsDescription() {
		return regionsDescription;
	}

	public void setRegionsDescription(String regionsDescription) {
		this.regionsDescription = regionsDescription;
	}

	public String getRegionsDescriptionRu() {
		return regionsDescriptionRu;
	}

	public void setRegionsDescriptionRu(String regionsDescriptionRu) {
		this.regionsDescriptionRu = regionsDescriptionRu;
	}

	public String getSettlementTypeDescription() {
		return settlementTypeDescription;
	}

	public void setSettlementTypeDescription(String settlementTypeDescription) {
		this.settlementTypeDescription = settlementTypeDescription;
	}

	public String getSettlementTypeDescriptionRu() {
		return settlementTypeDescriptionRu;
	}

	public void setSettlementTypeDescriptionRu(String settlementTypeDescriptionRu) {
		this.settlementTypeDescriptionRu = settlementTypeDescriptionRu;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
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
}

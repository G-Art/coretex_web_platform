package com.coretex.newpost.api.actions.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentListItemValue {

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "DateTime")
	private String dateTime;

	@JsonProperty(value = "PreferredDeliveryDate")
	private String preferredDeliveryDate;

	@JsonProperty(value = "Weight")
	private Integer weight;

	@JsonProperty(value = "SeatsAmount")
	private Integer seatsAmount;

	@JsonProperty(value = "IntDocNumber")
	private Integer intDocNumber;

	@JsonProperty(value = "Cost")
	private Integer cost;

	@JsonProperty(value = "CitySender")
	private String citySender;

	@JsonProperty(value = "CityRecipient")
	private String cityRecipient;

	@JsonProperty(value = "SenderAddress")
	private String senderAddress;

	@JsonProperty(value = "RecipientAddress")
	private String recipientAddress;

	@JsonProperty(value = "CostOnSite")
	private Integer costOnSite;

	@JsonProperty(value = "PayerType")
	private String payerType;

	@JsonProperty(value = "PaymentMethod")
	private String paymentMethod;

	@JsonProperty(value = "AfterpaymentOnGoodsCost")
	private String afterPaymentOnGoodsCost;

	@JsonProperty(value = "PackingNumber")
	private String packingNumber;

	@JsonProperty(value = "RejectionReason")
	private String rejectionReason;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getPreferredDeliveryDate() {
		return preferredDeliveryDate;
	}

	public void setPreferredDeliveryDate(String preferredDeliveryDate) {
		this.preferredDeliveryDate = preferredDeliveryDate;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getSeatsAmount() {
		return seatsAmount;
	}

	public void setSeatsAmount(Integer seatsAmount) {
		this.seatsAmount = seatsAmount;
	}

	public Integer getIntDocNumber() {
		return intDocNumber;
	}

	public void setIntDocNumber(Integer intDocNumber) {
		this.intDocNumber = intDocNumber;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public String getCitySender() {
		return citySender;
	}

	public void setCitySender(String citySender) {
		this.citySender = citySender;
	}

	public String getCityRecipient() {
		return cityRecipient;
	}

	public void setCityRecipient(String cityRecipient) {
		this.cityRecipient = cityRecipient;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getRecipientAddress() {
		return recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	public Integer getCostOnSite() {
		return costOnSite;
	}

	public void setCostOnSite(Integer costOnSite) {
		this.costOnSite = costOnSite;
	}

	public String getPayerType() {
		return payerType;
	}

	public void setPayerType(String payerType) {
		this.payerType = payerType;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getAfterPaymentOnGoodsCost() {
		return afterPaymentOnGoodsCost;
	}

	public void setAfterPaymentOnGoodsCost(String afterPaymentOnGoodsCost) {
		this.afterPaymentOnGoodsCost = afterPaymentOnGoodsCost;
	}

	public String getPackingNumber() {
		return packingNumber;
	}

	public void setPackingNumber(String packingNumber) {
		this.packingNumber = packingNumber;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
}

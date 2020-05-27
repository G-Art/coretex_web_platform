package com.coretex.commerce.data;

public class OrderData extends AbstractOrderData {

	private Boolean customerAgreement;
	private Boolean confirmedEmail;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getCustomerAgreement() {
		return customerAgreement;
	}

	public void setCustomerAgreement(Boolean customerAgreement) {
		this.customerAgreement = customerAgreement;
	}

	public Boolean getConfirmedEmail() {
		return confirmedEmail;
	}

	public void setConfirmedEmail(Boolean confirmedEmail) {
		this.confirmedEmail = confirmedEmail;
	}

}

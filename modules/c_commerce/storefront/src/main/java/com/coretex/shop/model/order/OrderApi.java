package com.coretex.shop.model.order;

import java.util.ArrayList;
import java.util.List;

import com.coretex.shop.model.entity.Entity;

public class OrderApi extends Entity {


	private static final long serialVersionUID = 1L;

	private boolean customerAgreement;
	private String comments;
	private String currency;
	private List<OrderAttribute> attributes = new ArrayList<OrderAttribute>();


	public boolean isCustomerAgreement() {
		return customerAgreement;
	}

	public void setCustomerAgreement(boolean customerAgreement) {
		this.customerAgreement = customerAgreement;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<OrderAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<OrderAttribute> attributes) {
		this.attributes = attributes;
	}


}

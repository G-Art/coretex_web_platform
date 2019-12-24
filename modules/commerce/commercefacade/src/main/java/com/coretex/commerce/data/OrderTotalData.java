package com.coretex.commerce.data;

import java.math.BigDecimal;

public class OrderTotalData extends GenericItemData {

	private String title;
	private String text;
	private BigDecimal value;
	private String orderTotalCode;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getOrderTotalCode() {
		return orderTotalCode;
	}

	public void setOrderTotalCode(String orderTotalCode) {
		this.orderTotalCode = orderTotalCode;
	}
}

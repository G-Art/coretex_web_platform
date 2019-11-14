package com.coretex.core.model.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderTotalPriceItem implements Serializable {


	private static final long serialVersionUID = 1L;
	private BigDecimal itemPrice;
	private String itemCode;

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemCode() {
		return itemCode;
	}

}

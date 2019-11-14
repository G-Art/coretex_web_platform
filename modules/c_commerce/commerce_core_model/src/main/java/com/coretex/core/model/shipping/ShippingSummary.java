package com.coretex.core.model.shipping;

import java.io.Serializable;
import java.math.BigDecimal;

import com.coretex.items.commerce_core_model.DeliveryItem;


public class ShippingSummary implements Serializable {


	private static final long serialVersionUID = 1L;
	private BigDecimal shipping;
	private BigDecimal handling;
	private String shippingModule;
	private String shippingOption;
	private String shippingOptionCode;
	private boolean freeShipping;
	private boolean taxOnShipping;

	private DeliveryItem deliveryAddress;


	public BigDecimal getShipping() {
		return shipping;
	}

	public void setShipping(BigDecimal shipping) {
		this.shipping = shipping;
	}

	public BigDecimal getHandling() {
		return handling;
	}

	public void setHandling(BigDecimal handling) {
		this.handling = handling;
	}

	public String getShippingModule() {
		return shippingModule;
	}

	public void setShippingModule(String shippingModule) {
		this.shippingModule = shippingModule;
	}

	public String getShippingOption() {
		return shippingOption;
	}

	public void setShippingOption(String shippingOption) {
		this.shippingOption = shippingOption;
	}

	public boolean isFreeShipping() {
		return freeShipping;
	}

	public void setFreeShipping(boolean freeShipping) {
		this.freeShipping = freeShipping;
	}

	public boolean isTaxOnShipping() {
		return taxOnShipping;
	}

	public void setTaxOnShipping(boolean taxOnShipping) {
		this.taxOnShipping = taxOnShipping;
	}

	public DeliveryItem getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryItem deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getShippingOptionCode() {
		return shippingOptionCode;
	}

	public void setShippingOptionCode(String shippingOptionCode) {
		this.shippingOptionCode = shippingOptionCode;
	}

}

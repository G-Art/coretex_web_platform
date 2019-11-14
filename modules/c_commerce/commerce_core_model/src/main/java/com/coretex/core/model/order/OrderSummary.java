package com.coretex.core.model.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.coretex.core.model.shipping.ShippingSummary;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;


/**
 * This object is used as input object for many services
 * such as order total calculation and tax calculation
 *
 * @author Carl Samson
 */
public class OrderSummary implements Serializable {

	private OrderSummaryType orderSummaryType = OrderSummaryType.ORDERTOTAL;
	private static final long serialVersionUID = 1L;
	private ShippingSummary shippingSummary;
	private List<ShoppingCartEntryItem> products = new ArrayList<ShoppingCartEntryItem>();

	public void setProducts(List<ShoppingCartEntryItem> products) {
		this.products = products;
	}

	public List<ShoppingCartEntryItem> getProducts() {
		return products;
	}

	public void setShippingSummary(ShippingSummary shippingSummary) {
		this.shippingSummary = shippingSummary;
	}

	public ShippingSummary getShippingSummary() {
		return shippingSummary;
	}

	public OrderSummaryType getOrderSummaryType() {
		return orderSummaryType;
	}

	public void setOrderSummaryType(OrderSummaryType orderSummaryType) {
		this.orderSummaryType = orderSummaryType;
	}

}

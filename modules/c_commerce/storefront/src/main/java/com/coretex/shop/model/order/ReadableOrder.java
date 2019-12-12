package com.coretex.shop.model.order;

import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.customer.ReadableDelivery;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.shop.model.order.total.OrderTotal;

import java.io.Serializable;
import java.util.List;


public class ReadableOrder extends OrderEntity implements Serializable {


	private static final long serialVersionUID = 1L;
	private ReadableCustomer customer;
	private List<ReadableOrderProduct> products;
	private CurrencyItem currencyModel;

	private Address billing;
	private ReadableDelivery delivery;


	public void setCustomer(ReadableCustomer customer) {
		this.customer = customer;
	}

	public ReadableCustomer getCustomer() {
		return customer;
	}

	public OrderTotal getTotal() {
		return total;
	}

	public void setTotal(OrderTotal total) {
		this.total = total;
	}

	public OrderTotal getTax() {
		return tax;
	}

	public void setTax(OrderTotal tax) {
		this.tax = tax;
	}

	public OrderTotal getShipping() {
		return shipping;
	}

	public void setShipping(OrderTotal shipping) {
		this.shipping = shipping;
	}

	public List<ReadableOrderProduct> getProducts() {
		return products;
	}

	public void setProducts(List<ReadableOrderProduct> products) {
		this.products = products;
	}

	public CurrencyItem getCurrencyModel() {
		return currencyModel;
	}

	public void setCurrencyModel(CurrencyItem currencyModel) {
		this.currencyModel = currencyModel;
	}

	public Address getBilling() {
		return billing;
	}

	public void setBilling(Address billing) {
		this.billing = billing;
	}

	public Address getDelivery() {
		return delivery;
	}

	public void setDelivery(ReadableDelivery delivery) {
		this.delivery = delivery;
	}

	private OrderTotal total;
	private OrderTotal tax;
	private OrderTotal shipping;

}

package com.coretex.shop.model.order;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.core.model.shipping.ShippingOption;
import com.coretex.core.model.shipping.ShippingSummary;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;


/**
 * Orders saved on the website
 *
 * @author Carl Samson
 */
public class ShopOrder extends PersistableOrder implements Serializable {


	private static final long serialVersionUID = 1L;
	private List<ShoppingCartEntryItem> shoppingCartItems;//overrides parent API list of shoppingcartitem

	private OrderTotalSummary orderTotalSummary;//The order total displayed to the end user. That object will be used when committing the order


	private ShippingSummary shippingSummary;
	private ShippingOption selectedShippingOption = null;//Default selected option

	private String defaultPaymentMethodCode = null;


	private String paymentMethodType = null;//user selected payment type
	private Map<String, String> payment;//user payment information

	private String errorMessage = null;
//=========================================
	private String deliveryMethod;
	private String city;
	private UUID cityCode;
	private UUID postOffice;

	private String phone;
	private String userName;
	private String email;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UUID getCityCode() {
		return cityCode;
	}

	public void setCityCode(UUID cityCode) {
		this.cityCode = cityCode;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public UUID getPostOffice() {
		return postOffice;
	}

	public void setPostOffice(UUID postOffice) {
		this.postOffice = postOffice;
	}

	public void setShoppingCartItems(List<ShoppingCartEntryItem> shoppingCartItems) {
		this.shoppingCartItems = shoppingCartItems;
	}

	public List<ShoppingCartEntryItem> getShoppingCartItems() {
		return shoppingCartItems;
	}

	public void setOrderTotalSummary(OrderTotalSummary orderTotalSummary) {
		this.orderTotalSummary = orderTotalSummary;
	}

	public OrderTotalSummary getOrderTotalSummary() {
		return orderTotalSummary;
	}

	public ShippingSummary getShippingSummary() {
		return shippingSummary;
	}

	public void setShippingSummary(ShippingSummary shippingSummary) {
		this.shippingSummary = shippingSummary;
	}

	public ShippingOption getSelectedShippingOption() {
		return selectedShippingOption;
	}

	public void setSelectedShippingOption(ShippingOption selectedShippingOption) {
		this.selectedShippingOption = selectedShippingOption;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getPaymentMethodType() {
		return paymentMethodType;
	}

	public void setPaymentMethodType(String paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}

	public Map<String, String> getPayment() {
		return payment;
	}

	public void setPayment(Map<String, String> payment) {
		this.payment = payment;
	}

	public String getDefaultPaymentMethodCode() {
		return defaultPaymentMethodCode;
	}

	public void setDefaultPaymentMethodCode(String defaultPaymentMethodCode) {
		this.defaultPaymentMethodCode = defaultPaymentMethodCode;
	}


}

package com.coretex.core.data.orders;

import com.coretex.items.commerce_core_model.BillingItem;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.enums.commerce_core_model.OrderStatusEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String orderHistoryComment = "";

	List<OrderStatusEnum> orderStatusList = Arrays.asList(OrderStatusEnum.values());
	private String datePurchased = "";
	private OrderItem order;

	private DeliveryItem delivery = null;

	private BillingItem billing = null;


	public String getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(String datePurchased) {
		this.datePurchased = datePurchased;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderHistoryComment() {
		return orderHistoryComment;
	}

	public void setOrderHistoryComment(String orderHistoryComment) {
		this.orderHistoryComment = orderHistoryComment;
	}

	public List<OrderStatusEnum> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<OrderStatusEnum> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public OrderItem getOrder() {
		return order;
	}

	public void setOrder(OrderItem order) {
		this.order = order;
	}

	public DeliveryItem getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryItem delivery) {
		this.delivery = delivery;
	}

	public BillingItem getBilling() {
		return billing;
	}

	public void setBilling(BillingItem billing) {
		this.billing = billing;
	}


}
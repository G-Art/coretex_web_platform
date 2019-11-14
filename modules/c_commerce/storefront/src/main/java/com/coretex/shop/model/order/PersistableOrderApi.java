package com.coretex.shop.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.coretex.shop.model.order.transaction.PersistablePayment;

import java.util.UUID;

/**
 * This object is used when processing an order from the API
 * It will be used for processing the payment and as OrderItem meta data
 *
 * @author c.samson
 */
public class PersistableOrderApi extends OrderApi {


	private static final long serialVersionUID = 1L;

	private PersistablePayment payment;
	private UUID shippingQuote;
	@JsonIgnore
	private UUID shoppingCartId;
	@JsonIgnore
	private UUID customerId;


	public UUID getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(UUID shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}

	public PersistablePayment getPayment() {
		return payment;
	}

	public void setPayment(PersistablePayment payment) {
		this.payment = payment;
	}

	public UUID getShippingQuote() {
		return shippingQuote;
	}

	public void setShippingQuote(UUID shippingQuote) {
		this.shippingQuote = shippingQuote;
	}


}

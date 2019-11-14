package com.coretex.newpost.facades;

import com.coretex.core.model.shipping.ShippingSummary;
import com.coretex.items.commerce_core_model.ShoppingCartItem;

public interface NewPostFacade {
	ShippingSummary calculateShippingSummary(String shippingMethod, String city, ShoppingCartItem cart);
}

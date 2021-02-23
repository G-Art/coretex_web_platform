package com.coretex.commerce.facades;

import com.coretex.commerce.data.CartData;
import com.coretex.commerce.data.OrderPlaceResult;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.CustomerItem;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.UUID;

public interface CartFacade extends PageableDataTableFacade<CartItem, CartData> {

	CartData getByUUID(UUID uuid);

	CartData getOrCreateCart(UUID uuid, String storeDomain, CustomerItem customer);

	CartData createCart(String storeDomain, CustomerItem customer);

	Flux<CartData> getCartsForCustomer(UUID customerUuid);

	CartData updateCart(CartData cartData, UUID entry, Integer quantity);

	CartData addToCart(CartData cartData, UUID product, Integer quantity);

	CartData setDeliveryType(CartData cartData, UUID deliveryType);

	CartData saveDeliveryInfo(CartData cartData, Map<String, Object> info);

	CartData merge(CartData sessionCart, CartData userCart);

	CartData setPaymentType(CartData cartData, UUID paymentMode);

	OrderPlaceResult palaceOrder(CartData cartData);

	void delete(UUID uuid);
}

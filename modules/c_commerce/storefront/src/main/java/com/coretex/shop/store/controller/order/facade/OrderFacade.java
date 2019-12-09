package com.coretex.shop.store.controller.order.facade;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import org.springframework.validation.BindingResult;


import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.core.model.shipping.ShippingQuote;
import com.coretex.core.model.shipping.ShippingSummary;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.shop.model.order.PersistableOrder;
import com.coretex.shop.model.order.PersistableOrderApi;
import com.coretex.shop.model.order.ReadableOrder;
import com.coretex.shop.model.order.ReadableOrderList;
import com.coretex.shop.model.order.ShopOrder;
import com.coretex.shop.model.order.transaction.ReadableTransaction;


public interface OrderFacade {

	ShopOrder initializeOrder(MerchantStoreItem store, CustomerItem customer, ShoppingCartItem shoppingCart, LocaleItem language) throws Exception;

	void refreshOrder(ShopOrder order, MerchantStoreItem store, CustomerItem customer, ShoppingCartItem shoppingCart, LocaleItem language) throws Exception;

	/**
	 * used in website
	 **/
	OrderTotalSummary calculateOrderTotal(MerchantStoreItem store, ShopOrder order, LocaleItem language) throws Exception;

	/**
	 * used in the API
	 **/
	OrderTotalSummary calculateOrderTotal(MerchantStoreItem store, PersistableOrder order, LocaleItem language) throws Exception;

	OrderTotalSummary calculateOrderTotal(MerchantStoreItem store, CustomerItem customer, PersistableOrder order, LocaleItem language) throws Exception;

	/**
	 * process a valid order
	 **/
	OrderItem processOrder(ShopOrder order, CustomerItem customer, MerchantStoreItem store, LocaleItem language) ;

	/**
	 * process a valid order submitted from the API
	 **/
	OrderItem processOrder(PersistableOrderApi order, CustomerItem customer, MerchantStoreItem store, LocaleItem language, Locale locale) ;


	/**
	 * creates a working copy of customer when the user is anonymous
	 **/
	CustomerItem initEmptyCustomer(MerchantStoreItem store);

	List<CountryItem> getShipToCountry(MerchantStoreItem store, LocaleItem language)
			throws Exception;

	/**
	 * Creates a ShippingSummary object for OrderTotalItem calculation based on a ShippingQuote
	 *
	 * @param quote
	 * @param store
	 * @param language
	 * @return
	 */
	ShippingSummary getShippingSummary(ShippingQuote quote, MerchantStoreItem store, LocaleItem language);

	/**
	 * Validates an order submitted from the web application
	 *
	 * @param order
	 * @param bindingResult
	 * @param messagesResult
	 * @param store
	 * @param locale
	 * @
	 */
	void validateOrder(ShopOrder order, BindingResult bindingResult,
					   Map<String, String> messagesResult, MerchantStoreItem store,
					   Locale locale) ;

	/**
	 * Creates a ReadableOrder object from an orderId
	 *
	 * @param orderId
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableOrder getReadableOrder(UUID orderId, MerchantStoreItem store, LocaleItem language) throws Exception;


	/**
	 * <p>Method used to fetch all orders associated with customer customer.
	 * It will used current customer ID to fetch all orders which has been
	 * placed by customer for current store.</p>
	 *
	 * @param customer currently logged in customer
	 * @param store    store associated with current customer
	 * @return ReadableOrderList
	 * @throws Exception
	 */

	ReadableOrderList getReadableOrderList(MerchantStoreItem store, CustomerItem customer, int start,
										   int maxCount, LocaleItem language) throws Exception;


	/**
	 * <p>Method used to fetch all orders associated with customer customer.
	 * It will used current customer ID to fetch all orders which has been
	 * placed by customer for current store.</p>
	 *
	 * @return ReadableOrderList
	 * @throws Exception
	 */

	ReadableOrderList getReadableOrderList(int start, int maxCount, String draw) throws Exception;



	/**
	 * Get orders for a given store
	 *
	 * @param store
	 * @param start
	 * @param maxCount
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableOrderList getReadableOrderList(MerchantStoreItem store, int start,
										   int maxCount, LocaleItem language) throws Exception;
}

package com.coretex.core.business.services.order;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.core.model.order.OrderCriteria;
import com.coretex.core.model.order.OrderList;
import com.coretex.core.model.order.OrderSummary;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;


public interface OrderService extends SalesManagerEntityService<OrderItem> {

	Map getStaticForPeriod(Date from);

	void addOrderStatusHistory(OrderItem order, OrderStatusHistoryItem history)
			;

	/**
	 * Can be used to calculates the final prices of all items contained in checkout page
	 *
	 * @param orderSummary
	 * @param customer
	 * @param store
	 * @param language
	 * @return
	 * @
	 */
	OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
										 CustomerItem customer, MerchantStoreItem store, LocaleItem language)
			;

	/**
	 * Can be used to calculates the final prices of all items contained in a ShoppingCartItem
	 *
	 * @param orderSummary
	 * @param store
	 * @param language
	 * @return
	 * @
	 */
	OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
										 MerchantStoreItem store, LocaleItem language) ;


	/**
	 * Can be used to calculates the final prices of all items contained in checkout page
	 *
	 * @param shoppingCart
	 * @param customer
	 * @param store
	 * @param language
	 * @return @return {@link OrderTotalSummary}
	 * @
	 */
	OrderTotalSummary calculateShoppingCartTotal(final ShoppingCartItem shoppingCart, final CustomerItem customer, final MerchantStoreItem store, final LocaleItem language) ;

	/**
	 * Can be used to calculates the final prices of all items contained in a ShoppingCartItem
	 *
	 * @param shoppingCart
	 * @param store
	 * @param language
	 * @return {@link OrderTotalSummary}
	 * @
	 */
	OrderTotalSummary calculateShoppingCartTotal(final ShoppingCartItem shoppingCart, final MerchantStoreItem store, final LocaleItem language);

	ByteArrayOutputStream generateInvoice(MerchantStoreItem store, OrderItem order,
										  LocaleItem language) ;

	OrderItem getOrder(UUID id);


	/**
	 * For finding orders. Mainly used in the administration tool
	 *
	 * @param store
	 * @param criteria
	 * @return
	 */
	OrderList listByStore(MerchantStoreItem store, OrderCriteria criteria);


	/**
	 * get all orders. Mainly used in the administration tool
	 *
	 * @param criteria
	 * @return
	 */
	OrderList getOrders(OrderCriteria criteria);

	void saveOrUpdate(OrderItem order) ;

	OrderItem processOrder(OrderItem order, CustomerItem customer,
						   List<ShoppingCartEntryItem> items, OrderTotalSummary summary, MerchantStoreItem store) ;

}

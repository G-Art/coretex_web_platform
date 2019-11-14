package com.coretex.core.business.services.order;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.core.model.order.OrderCriteria;
import com.coretex.core.model.order.OrderList;
import com.coretex.core.model.order.OrderSummary;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.core.model.payments.Payment;
import com.coretex.items.commerce_core_model.TransactionItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;


public interface OrderService extends SalesManagerEntityService<OrderItem> {

	void addOrderStatusHistory(OrderItem order, OrderStatusHistoryItem history)
			throws ServiceException;

	/**
	 * Can be used to calculates the final prices of all items contained in checkout page
	 *
	 * @param orderSummary
	 * @param customer
	 * @param store
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
										 CustomerItem customer, MerchantStoreItem store, LanguageItem language)
			throws ServiceException;

	/**
	 * Can be used to calculates the final prices of all items contained in a ShoppingCartItem
	 *
	 * @param orderSummary
	 * @param store
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
										 MerchantStoreItem store, LanguageItem language) throws ServiceException;


	/**
	 * Can be used to calculates the final prices of all items contained in checkout page
	 *
	 * @param shoppingCart
	 * @param customer
	 * @param store
	 * @param language
	 * @return @return {@link OrderTotalSummary}
	 * @throws ServiceException
	 */
	OrderTotalSummary calculateShoppingCartTotal(final ShoppingCartItem shoppingCart, final CustomerItem customer, final MerchantStoreItem store, final LanguageItem language) throws ServiceException;

	/**
	 * Can be used to calculates the final prices of all items contained in a ShoppingCartItem
	 *
	 * @param shoppingCart
	 * @param store
	 * @param language
	 * @return {@link OrderTotalSummary}
	 * @throws ServiceException
	 */
	OrderTotalSummary calculateShoppingCartTotal(final ShoppingCartItem shoppingCart, final MerchantStoreItem store, final LanguageItem language) throws ServiceException;

	ByteArrayOutputStream generateInvoice(MerchantStoreItem store, OrderItem order,
										  LanguageItem language) throws ServiceException;

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

	void saveOrUpdate(OrderItem order) throws ServiceException;

	OrderItem processOrder(OrderItem order, CustomerItem customer,
						   List<ShoppingCartEntryItem> items, OrderTotalSummary summary,
						   Payment payment, MerchantStoreItem store) throws ServiceException;

	OrderItem processOrder(OrderItem order, CustomerItem customer,
						   List<ShoppingCartEntryItem> items, OrderTotalSummary summary,
						   Payment payment, TransactionItem transaction, MerchantStoreItem store)
			throws ServiceException;


	/**
	 * Determines if an OrderItem has download files
	 *
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	boolean hasDownloadFiles(OrderItem order) throws ServiceException;

	/**
	 * List all orders that have been pre-authorized but not captured
	 *
	 * @param store
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 */
	List<OrderItem> getCapturableOrders(MerchantStoreItem store, Date startDate, Date endDate) throws ServiceException;

}

package com.coretex.core.business.services.shipping;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.QuoteItem;
import com.coretex.core.model.shipping.ShippingSummary;

/**
 * Saves and retrieves various shipping quotes done by the system
 *
 * @author c.samson
 */
public interface ShippingQuoteService extends SalesManagerEntityService<QuoteItem> {

	/**
	 * Find shipping quotes by OrderItem
	 *
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	List<QuoteItem> findByOrder(OrderItem order) throws ServiceException;


	/**
	 * Each quote asked for a given shopping cart creates individual QuoteItem object
	 * in the table ShippingQuote. This method allows the creation of a ShippingSummary
	 * object to work with in the services and in the api.
	 *
	 * @param quoteId
	 * @return
	 * @throws ServiceException
	 */
	ShippingSummary getShippingSummary(UUID quoteId, MerchantStoreItem store) throws ServiceException;

}

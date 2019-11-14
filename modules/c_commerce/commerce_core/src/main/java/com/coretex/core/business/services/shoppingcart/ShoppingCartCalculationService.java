
package com.coretex.core.business.services.shoppingcart;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;

/**
 * Interface declaring various methods used to calculate {@link ShoppingCartItem}
 * object details.
 *
 * @author Umesh Awasthi
 * @since 1.2
 *
 */
public interface ShoppingCartCalculationService {
	/**
	 * Method which will be used to calculate price for each line items as well
	 * Total and Sub-total for {@link ShoppingCartItem}.
	 *
	 * @param cartModel
	 *            ShoopingCart mode representing underlying DB object
	 * @param customer
	 * @param store
	 * @param language
	 * @throws ServiceException
	 */
	OrderTotalSummary calculate(final ShoppingCartItem cartModel, final CustomerItem customer, final MerchantStoreItem store,
								final LanguageItem language) throws ServiceException;

	/**
	 * Method which will be used to calculate price for each line items as well
	 * Total and Sub-total for {@link ShoppingCartItem}.
	 *
	 * @param cartModel
	 *            ShoopingCart mode representing underlying DB object
	 * @param store
	 * @param language
	 * @throws ServiceException
	 */
	OrderTotalSummary calculate(final ShoppingCartItem cartModel, final MerchantStoreItem store, final LanguageItem language)
			throws ServiceException;
}

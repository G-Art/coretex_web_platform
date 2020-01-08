
package com.coretex.core.business.services.shoppingcart;


import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.items.core.LocaleItem;
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
	 * @
	 */
	OrderTotalSummary calculate(final ShoppingCartItem cartModel, final CustomerItem customer, final MerchantStoreItem store,
								final LocaleItem language) ;

	/**
	 * Method which will be used to calculate price for each line items as well
	 * Total and Sub-total for {@link ShoppingCartItem}.
	 *
	 * @param cartModel
	 *            ShoopingCart mode representing underlying DB object
	 * @param store
	 * @param language
	 * @
	 */
	OrderTotalSummary calculate(final ShoppingCartItem cartModel, final MerchantStoreItem store, final LocaleItem language);
}

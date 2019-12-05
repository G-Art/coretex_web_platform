
package com.coretex.core.business.services.shoppingcart;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.services.order.OrderServiceImpl;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.core.model.order.OrderTotalSummary;

/**
 * <p>
 * Implementation class responsible for calculating state of shopping cart. This
 * class will take care of calculating price of each line items of shopping cart
 * as well any discount including sub-total and total amount.
 * </p>
 *
 * @author Umesh Awasthi
 * @version 1.2
 */
@Service("shoppingCartCalculationService")
public class ShoppingCartCalculationServiceImpl implements ShoppingCartCalculationService {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	@Resource
	private ShoppingCartService shoppingCartService;

	@Resource
	private OrderService orderService;

	/**
	 * <p>
	 * Method used to recalculate state of shopping cart every time any change
	 * has been made to underlying {@link ShoppingCartItem} object in DB.
	 * </p>
	 * Following operations will be performed by this method.
	 *
	 * <li>Calculate price for each {@link ShoppingCartEntryItem} and update it.</li>
	 * <p>
	 * This method is backbone method for all price calculation related to
	 * shopping cart.
	 * </p>
	 *
	 * @see OrderServiceImpl
	 *
	 * @param cartModel
	 * @param customer
	 * @param store
	 * @param language
	 * @
	 */
	@Override
	public OrderTotalSummary calculate(final ShoppingCartItem cartModel, final CustomerItem customer, final MerchantStoreItem store,
									   final LocaleItem language)  {

		Validate.notNull(cartModel, "cart cannot be null");
		Validate.notNull(cartModel.getLineItems(), "Cart should have line items.");
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		Validate.notNull(customer, "CustomerItem cannot be null");
		OrderTotalSummary orderTotalSummary = orderService.calculateShoppingCartTotal(cartModel, customer, store,
				language);
		updateCartModel(cartModel);
		return orderTotalSummary;

	}

	/**
	 * <p>
	 * Method used to recalculate state of shopping cart every time any change
	 * has been made to underlying {@link ShoppingCartItem} object in DB.
	 * </p>
	 * Following operations will be performed by this method.
	 *
	 * <li>Calculate price for each {@link ShoppingCartEntryItem} and update it.</li>
	 * <p>
	 * This method is backbone method for all price calculation related to
	 * shopping cart.
	 * </p>
	 *
	 * @see OrderServiceImpl
	 *
	 * @param cartModel
	 * @param store
	 * @param language
	 * @
	 */
	@Override
	public OrderTotalSummary calculate(final ShoppingCartItem cartModel, final MerchantStoreItem store, final LocaleItem language) {

		Validate.notNull(cartModel, "cart cannot be null");
		Validate.notNull(cartModel.getLineItems(), "Cart should have line items.");
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		OrderTotalSummary orderTotalSummary = orderService.calculateShoppingCartTotal(cartModel, store, language);
		updateCartModel(cartModel);
		return orderTotalSummary;

	}

	public ShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}

	private void updateCartModel(final ShoppingCartItem cartModel) {
		shoppingCartService.saveOrUpdate(cartModel);
	}

}

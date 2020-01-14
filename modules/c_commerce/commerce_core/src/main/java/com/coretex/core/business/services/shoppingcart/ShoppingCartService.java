package com.coretex.core.business.services.shoppingcart;

import java.util.List;
import java.util.UUID;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.shipping.ShippingProduct;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;

public interface ShoppingCartService extends SalesManagerEntityService<ShoppingCartItem> {

	ShoppingCartItem getShoppingCart(CustomerItem customer);

	void saveOrUpdate(ShoppingCartItem shoppingCart);

	ShoppingCartItem getById(UUID id, MerchantStoreItem store);

	ShoppingCartItem getByCode(String code, MerchantStoreItem store);

	ShoppingCartItem getByCustomer(CustomerItem customer) ;

	/**
	 * Creates a list of ShippingProduct based on the ShoppingCartItem if items are
	 * virtual return list will be null
	 *
	 * @param cart
	 * @return
	 * @
	 */
	List<ShippingProduct> createShippingProduct(ShoppingCartItem cart) ;

	/**
	 * Looks if the items in the ShoppingCartItem are free of charges
	 *
	 * @param cart
	 * @return
	 * @
	 */
	boolean isFreeShoppingCart(ShoppingCartItem cart) ;

	boolean isFreeShoppingCart(List<ShoppingCartEntryItem> items) ;

	/**
	 * Populates a ShoppingCartEntryItem from a ProductItem and attributes if any
	 *
	 * @param product
	 * @return
	 * @
	 */
	ShoppingCartEntryItem populateShoppingCartItem(ProductItem product);

	void deleteCart(ShoppingCartItem cart) ;

	void removeShoppingCart(ShoppingCartItem cart) ;

	/**
	 * @param userShoppingModel
	 * @param sessionCart
	 * @param store
	 * @return {@link ShoppingCartItem} merged Shopping Cart
	 * @throws Exception
	 */
	ShoppingCartItem mergeShoppingCarts(final ShoppingCartItem userShoppingCart, final ShoppingCartItem sessionCart,
										final MerchantStoreItem store) throws Exception;

	/**
	 * Determines if the shopping cart requires shipping
	 *
	 * @param cart
	 * @return
	 * @
	 */
	boolean requiresShipping(ShoppingCartItem cart) ;

	/**
	 * Removes a shopping cart item
	 *
	 * @param item
	 * @param id
	 */
	void deleteShoppingCartItem(UUID id);

}
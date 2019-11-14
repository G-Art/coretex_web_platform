package com.coretex.core.business.services.shoppingcart;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.shipping.ShippingProduct;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;

public interface ShoppingCartService extends SalesManagerEntityService<ShoppingCartItem> {

	ShoppingCartItem getShoppingCart(CustomerItem customer) throws ServiceException;

	void saveOrUpdate(ShoppingCartItem shoppingCart) throws ServiceException;

	ShoppingCartItem getById(UUID id, MerchantStoreItem store) throws ServiceException;

	ShoppingCartItem getByCode(String code, MerchantStoreItem store) throws ServiceException;

	ShoppingCartItem getByCustomer(CustomerItem customer) throws ServiceException;

	/**
	 * Creates a list of ShippingProduct based on the ShoppingCartItem if items are
	 * virtual return list will be null
	 *
	 * @param cart
	 * @return
	 * @throws ServiceException
	 */
	List<ShippingProduct> createShippingProduct(ShoppingCartItem cart) throws ServiceException;

	/**
	 * Looks if the items in the ShoppingCartItem are free of charges
	 *
	 * @param cart
	 * @return
	 * @throws ServiceException
	 */
	boolean isFreeShoppingCart(ShoppingCartItem cart) throws ServiceException;

	boolean isFreeShoppingCart(List<ShoppingCartEntryItem> items) throws ServiceException;

	/**
	 * Populates a ShoppingCartEntryItem from a ProductItem and attributes if any
	 *
	 * @param product
	 * @return
	 * @throws ServiceException
	 */
	ShoppingCartEntryItem populateShoppingCartItem(ProductItem product) throws ServiceException;

	void deleteCart(ShoppingCartItem cart) throws ServiceException;

	void removeShoppingCart(ShoppingCartItem cart) throws ServiceException;

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
	 * @throws ServiceException
	 */
	boolean requiresShipping(ShoppingCartItem cart) throws ServiceException;

	/**
	 * Removes a shopping cart item
	 *
	 * @param item
	 * @param id
	 */
	void deleteShoppingCartItem(UUID id);

}
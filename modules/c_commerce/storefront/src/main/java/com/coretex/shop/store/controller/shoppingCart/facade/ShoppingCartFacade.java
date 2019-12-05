
package com.coretex.shop.store.controller.shoppingCart.facade;

import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.shop.model.shoppingcart.PersistableShoppingCartItem;
import com.coretex.shop.model.shoppingcart.ReadableShoppingCart;
import com.coretex.shop.model.shoppingcart.ShoppingCartData;

import java.util.List;
import java.util.UUID;

/**
 * </p>Shopping cart Facade which provide abstraction layer between
 * SM core module and Controller.
 * Only Data Object will be exposed to controller by hiding model
 * object from view.</p>
 * @author Umesh Awasthi
 * @author Carl Samson
 * @version 1.0
 * @since1.0
 *
 */


public interface ShoppingCartFacade {

	ShoppingCartData addItemsToShoppingCart(ShoppingCartData shoppingCart, final com.coretex.shop.model.shoppingcart.ShoppingCartItem item, final MerchantStoreItem store, final LocaleItem language, final CustomerItem customer) throws Exception;

	ShoppingCartItem createCartModel(final String shoppingCartCode, final MerchantStoreItem store, final CustomerItem customer) throws Exception;

	/**
	 * Method responsible for getting shopping cart from
	 * either session or from underlying DB.
	 */
	ShoppingCartData getShoppingCartData(final CustomerItem customer, final MerchantStoreItem store, final String shoppingCartId, LocaleItem language) throws Exception;

	ShoppingCartData getShoppingCartData(final ShoppingCartItem shoppingCart, LocaleItem language) throws Exception;

	ShoppingCartData getShoppingCartData(String code, MerchantStoreItem store, LocaleItem lnguage) throws Exception;

	ShoppingCartData removeCartItem(final UUID itemID, final String cartId, final MerchantStoreItem store, final LocaleItem language) throws Exception;

	ShoppingCartData updateCartItem(final UUID itemID, final String cartId, final long quantity, final MerchantStoreItem store, LocaleItem language) throws Exception;

	void deleteShoppingCart(final UUID id, final MerchantStoreItem store) throws Exception;

	ShoppingCartData updateCartItems(List<com.coretex.shop.model.shoppingcart.ShoppingCartItem> shoppingCartItems,
									 MerchantStoreItem store, LocaleItem language) throws Exception;

	ShoppingCartItem getShoppingCartModel(final String shoppingCartCode, MerchantStoreItem store);

	ShoppingCartItem getShoppingCartModel(UUID id, MerchantStoreItem store) throws Exception;

	ShoppingCartItem getShoppingCartModel(final CustomerItem customer, MerchantStoreItem store) throws Exception;

	void deleteShoppingCart(String code, MerchantStoreItem store) throws Exception;

	void saveOrUpdateShoppingCart(ShoppingCartItem cart) throws Exception;

	/**
	 * Get ShoppingCartItem
	 * This method is used by the API
	 * @param customer
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableShoppingCart getCart(CustomerItem customer, MerchantStoreItem store, LocaleItem language) throws Exception;

	/**
	 * Modify an item to an existing cart, quantity of line item will reflect item.getQuantity
	 * @param cartCode
	 * @param item
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableShoppingCart addToCart(String cartCode, PersistableShoppingCartItem item, MerchantStoreItem store,
								   LocaleItem language) throws Exception;

	/**
	 * Add item to shopping cart
	 * @param item
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableShoppingCart addToCart(PersistableShoppingCartItem item, MerchantStoreItem store,
								   LocaleItem language) throws Exception;

	/**
	 * Add product to ShoppingCartItem
	 * This method is used by the API
	 * @param customer
	 * @param item
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableShoppingCart addToCart(CustomerItem customer, PersistableShoppingCartItem item, MerchantStoreItem store, LocaleItem language) throws Exception;

	/**
	 * Retrieves a shopping cart by ID
	 * @param shoppingCartId
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableShoppingCart getById(UUID shoppingCartId, MerchantStoreItem store, LocaleItem language) throws Exception;

	/**
	 * Retrieves a shopping cart
	 * @param code
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableShoppingCart getByCode(String code, MerchantStoreItem store, LocaleItem language) throws Exception;
}

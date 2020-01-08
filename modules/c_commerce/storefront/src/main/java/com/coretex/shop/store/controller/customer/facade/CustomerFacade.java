
package com.coretex.shop.store.controller.customer.facade;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.shop.model.customer.CustomerEntity;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.customer.address.Address;

import java.util.UUID;

public interface CustomerFacade {

	/**
	 * Method used to fetch customer based on the username and storecode.
	 * CustomerItem username is unique to each store.
	 *
	 * @param userName
	 * @param store
	 * @param store
	 * @param language
	 * @throws Exception
	 *
	 */
	CustomerEntity getCustomerDataByUserName(final String userName, final MerchantStoreItem store, final LocaleItem language) throws Exception;

	/**
	 * Creates a ReadableCustomer
	 * @param id
	 * @param merchantStore
	 * @param language
	 * @return
	 */
	ReadableCustomer getCustomerById(final UUID id, final MerchantStoreItem merchantStore, final LocaleItem language);

	/**
	 * Get CustomerItem using unique username
	 * @param userName
	 * @param merchantStore
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableCustomer getByUserName(String userName, MerchantStoreItem merchantStore, LocaleItem language);

	/**
	 * <p>Method responsible for merging cart during authentication,
	 *     Method will perform following operations
	 * <li> Merge CustomerItem Shopping Cart with Session Cart if any.</li>
	 * <li> Convert CustomerItem to {@link CustomerEntity} </li>
	 * </p>
	 *
	 * @param customer username of CustomerItem
	 * @param sessionShoppingCartId session shopping cart, if user already have few items in Cart.
	 * @throws Exception
	 */
	ShoppingCartItem mergeCart(final CustomerItem customer, final String sessionShoppingCartId, final MerchantStoreItem store, final LocaleItem language) throws Exception;

	CustomerItem getCustomerByUserName(final String userName, final MerchantStoreItem store) throws Exception;

	boolean checkIfUserExists(final String userName, final MerchantStoreItem store) throws Exception;

	PersistableCustomer registerCustomer(final PersistableCustomer customer, final MerchantStoreItem merchantStore, final LocaleItem language) throws Exception;

	Address getAddress(final UUID userId, final MerchantStoreItem merchantStore, boolean isBillingAddress) throws Exception;

	void updateAddress(UUID userId, MerchantStoreItem merchantStore, Address address, final LocaleItem language)
			throws Exception;

	void setCustomerModelDefaultProperties(CustomerItem customer, MerchantStoreItem store) throws Exception;


	void authenticate(CustomerItem customer, String userName, String password) throws Exception;

	CustomerItem getCustomerModel(PersistableCustomer customer,
								  MerchantStoreItem merchantStore, LocaleItem language) throws Exception;

	/*
	 * Creates a CustomerItem from a PersistableCustomer received from REST API
	 */
	PersistableCustomer create(PersistableCustomer customer, MerchantStoreItem store);

	/**
	 * Reset customer password
	 * @param customer
	 * @param store
	 * @param language
	 * @throws Exception
	 */
	void resetPassword(CustomerItem customer, MerchantStoreItem store, LocaleItem language) throws Exception;

	/**
	 * Updates a CustomerItem
	 * @param customer
	 * @param store
	 * @throws Exception
	 */
	PersistableCustomer update(PersistableCustomer customer, MerchantStoreItem store);

	void delete(CustomerItem entity);

}


package com.coretex.shop.store.controller.customer.facade;

import com.coretex.core.model.customer.CustomerCriteria;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.populator.customer.ReadableCustomerList;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.shop.model.customer.CustomerEntity;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.customer.PersistableCustomerReview;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.customer.ReadableCustomerReview;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.shop.model.customer.optin.PersistableCustomerOptin;

/**
 * <p>CustomerItem facade working as a bridge between {@link CustomerService} and Controller
 * It will take care about interacting with Service API and doing any pre and post processing
 * </p>
 *
 * @author Umesh Awasthi
 * @version 2.2.1
 *
 *
 */
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
	CustomerEntity getCustomerDataByUserName(final String userName, final MerchantStoreItem store, final LanguageItem language) throws Exception;

	/**
	 * Creates a ReadableCustomer
	 * @param id
	 * @param merchantStore
	 * @param language
	 * @return
	 */
	ReadableCustomer getCustomerById(final UUID id, final MerchantStoreItem merchantStore, final LanguageItem language);

	/**
	 * Get CustomerItem using unique username
	 * @param userName
	 * @param merchantStore
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableCustomer getByUserName(String userName, MerchantStoreItem merchantStore, LanguageItem language);

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
	ShoppingCartItem mergeCart(final CustomerItem customer, final String sessionShoppingCartId, final MerchantStoreItem store, final LanguageItem language) throws Exception;

	CustomerItem getCustomerByUserName(final String userName, final MerchantStoreItem store) throws Exception;

	boolean checkIfUserExists(final String userName, final MerchantStoreItem store) throws Exception;

	PersistableCustomer registerCustomer(final PersistableCustomer customer, final MerchantStoreItem merchantStore, final LanguageItem language) throws Exception;

	Address getAddress(final UUID userId, final MerchantStoreItem merchantStore, boolean isBillingAddress) throws Exception;

	void updateAddress(UUID userId, MerchantStoreItem merchantStore, Address address, final LanguageItem language)
			throws Exception;

	void setCustomerModelDefaultProperties(CustomerItem customer, MerchantStoreItem store) throws Exception;


	void authenticate(CustomerItem customer, String userName, String password) throws Exception;

	CustomerItem getCustomerModel(PersistableCustomer customer,
								  MerchantStoreItem merchantStore, LanguageItem language) throws Exception;

	CustomerItem populateCustomerModel(CustomerItem customerModel, PersistableCustomer customer,
									   MerchantStoreItem merchantStore, LanguageItem language) throws Exception;

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
	void resetPassword(CustomerItem customer, MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Updates a CustomerItem
	 * @param customer
	 * @param store
	 * @throws Exception
	 */
	PersistableCustomer update(PersistableCustomer customer, MerchantStoreItem store);

	/**
	 * Save or update a CustomerReviewItem
	 * @param review
	 * @param store
	 * @param language
	 * @throws Exception
	 */
	PersistableCustomerReview saveOrUpdateCustomerReview(PersistableCustomerReview review, MerchantStoreItem store, LanguageItem language);

	/**
	 * List all customer reviews by reviewed
	 * @param customer
	 * @param customerId
	 * @param store
	 * @param language
	 * @return
	 */
	List<ReadableCustomerReview> getAllCustomerReviewsByReviewed(UUID customerId, MerchantStoreItem store, LanguageItem language);

	/**
	 * Deletes a customer review
	 * @param review
	 * @param customerId
	 * @param reviewId
	 * @param store
	 * @param language
	 */
	void deleteCustomerReview(UUID customerId, UUID reviewId, MerchantStoreItem store, LanguageItem language);

	/**
	 * OptinItem a customer to newsletter
	 * @param optin
	 * @param store
	 * @throws Exception
	 */
	void optinCustomer(PersistableCustomerOptin optin, MerchantStoreItem store);

	ReadableCustomer getCustomerByNick(String userName, MerchantStoreItem merchantStore, LanguageItem language);

	void deleteByNick(String nick);

	void delete(CustomerItem entity);

	ReadableCustomerList getListByStore(MerchantStoreItem store, CustomerCriteria criteria, LanguageItem language);

	PersistableCustomerReview createCustomerReview(
			UUID customerId,
			PersistableCustomerReview review,
			MerchantStoreItem merchantStore,
			LanguageItem language);

	PersistableCustomerReview updateCustomerReview(UUID id, UUID reviewId, PersistableCustomerReview review, MerchantStoreItem store, LanguageItem language);
}

package com.coretex.core.business.services.catalog.product.relationship;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.core.model.catalog.product.relationship.ProductRelationshipType;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;

public interface ProductRelationshipService extends
		SalesManagerEntityService<ProductRelationshipItem> {

	void saveOrUpdate(ProductRelationshipItem relationship) throws ServiceException;

	/**
	 * Get product relationship List for a given type (RELATED, FEATURED...) and language allows
	 * to return the product description in the appropriate language
	 *
	 * @param store
	 * @param product
	 * @param type
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	List<ProductRelationshipItem> getByType(MerchantStoreItem store, ProductItem product,
											ProductRelationshipType type, LanguageItem language) throws ServiceException;

	/**
	 * Get product relationship List for a given type (RELATED, FEATURED...) and a given base product
	 *
	 * @param store
	 * @param product
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	List<ProductRelationshipItem> getByType(MerchantStoreItem store, ProductItem product,
											ProductRelationshipType type)
			throws ServiceException;

	/**
	 * Get product relationship List for a given type (RELATED, FEATURED...)
	 *
	 * @param store
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	List<ProductRelationshipItem> getByType(MerchantStoreItem store,
											ProductRelationshipType type) throws ServiceException;

	List<ProductRelationshipItem> listByProduct(ProductItem product);

	List<ProductRelationshipItem> getByType(MerchantStoreItem store,
											ProductRelationshipType type, LanguageItem language);

	/**
	 * Get a list of relationship acting as groups of products
	 *
	 * @param store
	 * @return
	 */
	List<ProductRelationshipItem> getGroups(MerchantStoreItem store);

	/**
	 * Creates a product group
	 *
	 * @param groupName
	 * @throws ServiceException
	 */
	void addGroup(MerchantStoreItem store, String groupName) throws ServiceException;

	List<ProductRelationshipItem> getByGroup(MerchantStoreItem store, String groupName)
			throws ServiceException;

	void deleteGroup(MerchantStoreItem store, String groupName)
			throws ServiceException;

	void deactivateGroup(MerchantStoreItem store, String groupName)
			throws ServiceException;

	void activateGroup(MerchantStoreItem store, String groupName)
			throws ServiceException;

	List<ProductRelationshipItem> getByGroup(MerchantStoreItem store, String groupName,
											 LanguageItem language) throws ServiceException;

}

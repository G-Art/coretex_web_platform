package com.coretex.core.business.services.catalog.product.relationship;

import java.util.List;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.core.model.catalog.product.relationship.ProductRelationshipType;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.core.LocaleItem;

public interface ProductRelationshipService extends
		SalesManagerEntityService<ProductRelationshipItem> {

	void saveOrUpdate(ProductRelationshipItem relationship) ;

	/**
	 * Get product relationship List for a given type (RELATED, FEATURED...) and language allows
	 * to return the product description in the appropriate language
	 *
	 * @param store
	 * @param product
	 * @param type
	 * @param language
	 * @return
	 * @
	 */
	List<ProductRelationshipItem> getByType(MerchantStoreItem store, ProductItem product,
											ProductRelationshipType type, LocaleItem language) ;

	/**
	 * Get product relationship List for a given type (RELATED, FEATURED...) and a given base product
	 *
	 * @param store
	 * @param product
	 * @param type
	 * @return
	 * @
	 */
	List<ProductRelationshipItem> getByType(MerchantStoreItem store, ProductItem product,
											ProductRelationshipType type)
			;

	/**
	 * Get product relationship List for a given type (RELATED, FEATURED...)
	 *
	 * @param store
	 * @param type
	 * @return
	 * @
	 */
	List<ProductRelationshipItem> getByType(MerchantStoreItem store,
											ProductRelationshipType type) ;

	List<ProductRelationshipItem> listByProduct(ProductItem product);

	List<ProductRelationshipItem> getByType(MerchantStoreItem store,
											ProductRelationshipType type, LocaleItem language);

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
	 * @
	 */
	void addGroup(MerchantStoreItem store, String groupName) ;

	List<ProductRelationshipItem> getByGroup(MerchantStoreItem store, String groupName)
			;

	void deleteGroup(MerchantStoreItem store, String groupName)
			;

	void deactivateGroup(MerchantStoreItem store, String groupName)
			;

	void activateGroup(MerchantStoreItem store, String groupName)
			;

	List<ProductRelationshipItem> getByGroup(MerchantStoreItem store, String groupName,
											 LocaleItem language) ;

}

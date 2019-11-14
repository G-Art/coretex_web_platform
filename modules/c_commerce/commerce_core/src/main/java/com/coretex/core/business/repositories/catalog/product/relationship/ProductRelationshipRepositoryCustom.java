package com.coretex.core.business.repositories.catalog.product.relationship;

import java.util.List;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;


public interface ProductRelationshipRepositoryCustom {

	List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type,
											LanguageItem language);

	List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type,
											ProductItem product, LanguageItem language);

	List<ProductRelationshipItem> getByGroup(MerchantStoreItem store, String group);

	List<ProductRelationshipItem> getGroups(MerchantStoreItem store);

	List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type);

	List<ProductRelationshipItem> listByProducts(ProductItem product);

	List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type,
											ProductItem product);


}

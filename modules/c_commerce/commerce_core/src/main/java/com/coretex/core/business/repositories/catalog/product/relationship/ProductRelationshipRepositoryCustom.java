package com.coretex.core.business.repositories.catalog.product.relationship;

import java.util.List;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;


public interface ProductRelationshipRepositoryCustom {

	List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type,
											LocaleItem language);

	List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type,
											ProductItem product, LocaleItem language);

	List<ProductRelationshipItem> getByGroup(MerchantStoreItem store, String group);

	List<ProductRelationshipItem> getGroups(MerchantStoreItem store);

	List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type);

	List<ProductRelationshipItem> listByProducts(ProductItem product);

	List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type,
											ProductItem product);


}

package com.coretex.core.business.repositories.catalog.product;

import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.core.model.catalog.product.ProductList;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public interface ProductRepositoryCustom {


	ProductList listByStore(MerchantStoreItem store, LanguageItem language,
							ProductCriteria criteria);

	ProductItem getByFriendlyUrl(MerchantStoreItem store, String seUrl, Locale locale);

	List<ProductItem> getProductsListByCategories(@SuppressWarnings("rawtypes") Set categoryIds);

	List<ProductItem> getProductsListByCategories(Set<Long> categoryIds,
												  LanguageItem language);

	List<ProductItem> listByStore(MerchantStoreItem store);

	ProductItem getProductForLocale(UUID productId, LanguageItem language,
									Locale locale);


	ProductItem getByCode(String productCode, LanguageItem language);
	ProductItem getByCode(String productCode);

	List<ProductItem> getProductsForLocale(MerchantStoreItem store,
										   Set<UUID> categoryIds, LanguageItem language, Locale locale);

}

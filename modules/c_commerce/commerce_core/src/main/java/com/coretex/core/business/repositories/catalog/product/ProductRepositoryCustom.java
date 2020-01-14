package com.coretex.core.business.repositories.catalog.product;

import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.core.model.catalog.product.ProductList;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.cx_core.ProductItem;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public interface ProductRepositoryCustom {


	ProductList listByStore(MerchantStoreItem store, LocaleItem language,
							ProductCriteria criteria);

	ProductItem getByFriendlyUrl(MerchantStoreItem store, String seUrl, Locale locale);

	List<ProductItem> getProductsListByCategories(@SuppressWarnings("rawtypes") Set categoryIds);

	List<ProductItem> getProductsListByCategories(Set<Long> categoryIds,
												  LocaleItem language);

	List<ProductItem> listByStore(MerchantStoreItem store);

	ProductItem getProductForLocale(UUID productId, LocaleItem language,
									Locale locale);


	ProductItem getByCode(String productCode, LocaleItem language);
	ProductItem getByCode(String productCode);

	List<ProductItem> getProductsForLocale(MerchantStoreItem store,
										   Set<UUID> categoryIds, LocaleItem language, Locale locale);

}

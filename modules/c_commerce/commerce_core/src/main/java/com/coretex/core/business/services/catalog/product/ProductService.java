package com.coretex.core.business.services.catalog.product;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.core.model.catalog.product.ProductList;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;

import java.util.List;
import java.util.Locale;
import java.util.UUID;


public interface ProductService extends SalesManagerEntityService<ProductItem> {


	ProductItem getProductForLocale(UUID productId, LocaleItem language, Locale locale) ;

	List<ProductItem> getProductsForLocale(CategoryItem category, LocaleItem language, Locale locale) ;

	List<ProductItem> getProducts(List<UUID> categoryIds);


	ProductList listByStore(MerchantStoreItem store, LocaleItem language,
							ProductCriteria criteria);

	List<ProductItem> listByStore(MerchantStoreItem store);

	List<ProductItem> getProducts(List<UUID> categoryIds, LocaleItem language)
			;

	ProductItem getBySeUrl(MerchantStoreItem store, String seUrl, Locale locale);

	/**
	 * Get a product by sku (code) field  and the language
	 *
	 * @param productCode
	 * @return
	 */
	ProductItem getByCode(String productCode);


}
	
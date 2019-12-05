package com.coretex.core.business.services.catalog.product.file;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.DigitalProductItem;
import com.coretex.core.model.content.InputContentFile;
import com.coretex.items.commerce_core_model.MerchantStoreItem;


public interface DigitalProductService extends SalesManagerEntityService<DigitalProductItem> {

	void saveOrUpdate(DigitalProductItem digitalProduct) ;

	void addProductFile(ProductItem product, DigitalProductItem digitalProduct,
						InputContentFile inputFile) ;


	DigitalProductItem getByProduct(MerchantStoreItem store, ProductItem product)
			;


}

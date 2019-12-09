package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.data.minimal.MinimalProductData;
import com.coretex.commerce.admin.data.ProductData;
import com.coretex.items.commerce_core_model.ProductItem;

import java.util.List;

public interface ProductFacade extends PageableDataTableFacade<ProductItem, MinimalProductData> {

	ProductData getByCode(String code);

	Long count();
	List<ProductData> getAll();
}

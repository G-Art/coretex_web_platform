package com.coretex.commerce.facades;

import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.data.ProductData;
import com.coretex.items.cx_core.ProductItem;

import java.util.List;

public interface ProductFacade extends PageableDataTableFacade<ProductItem, MinimalProductData> {

	ProductData getByCode(String code);

	Long count();
	List<ProductData> getAll();
}

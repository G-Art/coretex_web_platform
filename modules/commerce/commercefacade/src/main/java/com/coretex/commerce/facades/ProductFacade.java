package com.coretex.commerce.facades;

import com.coretex.commerce.data.SearchPageResult;
import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.data.ProductData;
import com.coretex.items.cx_core.ProductItem;

import java.util.List;

public interface ProductFacade extends PageableDataTableFacade<ProductItem, MinimalProductData> {

	SearchPageResult getCategoryPage(String code, int page, int size);

	ProductData getByCode(String code);

	Long count();
	List<ProductData> getAll();
}

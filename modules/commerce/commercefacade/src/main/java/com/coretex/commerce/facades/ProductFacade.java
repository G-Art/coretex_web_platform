package com.coretex.commerce.facades;

import com.coretex.commerce.data.DataTableResults;
import com.coretex.commerce.data.ProductData;
import com.coretex.commerce.data.SearchPageResult;
import com.coretex.commerce.data.forms.ProductForm;
import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.items.cx_core.ProductItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public interface ProductFacade extends PageableDataTableFacade<ProductItem, MinimalProductData> {

	SearchPageResult getCategoryPage(String code, int page, int size, Map<String, List<String>> filter, Map<String, List<String>> sort);

	ProductData getByCode(String code);
	ProductData getByUUID(UUID uuid);

	DataTableResults<MinimalProductData> getVariantsForProduct(UUID uuid, String draw, long page, Long length);

	Long count();
	Stream<ProductData> getAll();

	ProductItem save(ProductForm productform, UUID uuid);
}

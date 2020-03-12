package com.coretex.commerce.core.services;

import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;

import java.util.UUID;

public interface ProductService extends GenericItemService<ProductItem>  {
	ProductItem getByCode(String code);

	PageableSearchResult<VariantProductItem> variants(UUID uuid, long count, long page);

	PageableSearchResult<ProductItem> categoryPage(String code, long count, long page);

	ProductImageItem getDefaultImage(ProductItem productItem);
}

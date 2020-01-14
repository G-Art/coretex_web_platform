package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.ProductItem;

public interface ProductService extends GenericItemService<ProductItem>  {
	ProductItem getByCode(String code);
}

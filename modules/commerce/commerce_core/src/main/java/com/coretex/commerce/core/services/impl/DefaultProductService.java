package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.ProductDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.ProductService;
import com.coretex.items.cx_core.ProductItem;
import org.springframework.stereotype.Service;

@Service
public class DefaultProductService extends AbstractGenericItemService<ProductItem> implements ProductService {

	private ProductDao productDao;

	public DefaultProductService(ProductDao repository) {
		super(repository);
		this.productDao = repository;
	}

	@Override
	public ProductItem getByCode(String code) {
		return productDao.getByCode(code);
	}
}

package com.coretex.commerce.facades.impl;

import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.data.ProductData;
import com.coretex.commerce.facades.ProductFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalProductDataMapper;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.items.commerce_core_model.ProductItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("productFacade")
public class DefaultProductFacade implements ProductFacade {

	@Resource
	private ProductService productService;

	@Resource
	private MinimalProductDataMapper minimalProductDataMapper;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultProductFacade.class);

	@Override
	public ProductData getByCode(String code) {
		return null;
	}

	@Override
	public Long count() {
		return productService.count();
	}

	@Override
	public List<ProductData> getAll() {
		return null;
	}


	@Override
	public PageableService<ProductItem> getPageableService() {
		return productService;
	}

	@Override
	public GenericDataMapper<ProductItem, MinimalProductData> getDataMapper() {
		return minimalProductDataMapper;
	}

}

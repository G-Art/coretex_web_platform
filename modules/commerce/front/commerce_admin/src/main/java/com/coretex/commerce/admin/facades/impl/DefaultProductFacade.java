package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MinimalMerchantStoreData;
import com.coretex.commerce.admin.data.MinimalProductData;
import com.coretex.commerce.admin.data.ProductData;
import com.coretex.commerce.admin.facades.ProductFacade;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.MinimalProductDataMapper;
import com.coretex.commerce.admin.mapper.MerchantStoreDataMapper;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.common.generic.PageableEntityService;
import com.coretex.items.commerce_core_model.ProductItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
	public PageableEntityService<ProductItem> getPageableService() {
		return productService;
	}

	@Override
	public GenericDataMapper<ProductItem, MinimalProductData> getDataMapper() {
		return minimalProductDataMapper;
	}

}

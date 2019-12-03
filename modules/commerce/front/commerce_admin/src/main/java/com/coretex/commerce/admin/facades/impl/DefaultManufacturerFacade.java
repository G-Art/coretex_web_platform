package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.data.MinimalManufacturerData;
import com.coretex.commerce.admin.data.MinimalProductData;
import com.coretex.commerce.admin.data.ProductData;
import com.coretex.commerce.admin.facades.ManufacturerFacade;
import com.coretex.commerce.admin.facades.ProductFacade;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.MinimalManufacturerDataMapper;
import com.coretex.commerce.admin.mapper.MinimalProductDataMapper;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.core.business.services.common.generic.PageableEntityService;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.ProductItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("manufacturerFacade")
public class DefaultManufacturerFacade implements ManufacturerFacade {

	@Resource
	private ManufacturerService manufacturerService;

	@Resource
	private MinimalManufacturerDataMapper minimalManufacturerDataMapper;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultManufacturerFacade.class);

	@Override
	public ProductData getByCode(String code) {
		return null;
	}

	@Override
	public Long count() {
		return manufacturerService.count();
	}

	@Override
	public List<ProductData> getAll() {
		return null;
	}

	@Override
	public PageableEntityService<ManufacturerItem> getPageableService() {
		return manufacturerService;
	}

	@Override
	public GenericDataMapper<ManufacturerItem, MinimalManufacturerData> getDataMapper() {
		return minimalManufacturerDataMapper;
	}
}

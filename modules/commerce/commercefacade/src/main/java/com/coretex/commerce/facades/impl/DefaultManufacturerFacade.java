package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.ManufacturerService;
import com.coretex.commerce.data.minimal.MinimalManufacturerData;
import com.coretex.commerce.data.ProductData;
import com.coretex.commerce.facades.ManufacturerFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalManufacturerDataMapper;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.items.cx_core.ManufacturerItem;
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
	public PageableService<ManufacturerItem> getPageableService() {
		return manufacturerService;
	}

	@Override
	public GenericDataMapper<ManufacturerItem, MinimalManufacturerData> getDataMapper() {
		return minimalManufacturerDataMapper;
	}
}

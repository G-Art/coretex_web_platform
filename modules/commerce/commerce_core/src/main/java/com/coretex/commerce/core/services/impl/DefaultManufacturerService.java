package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.ManufacturerDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.ManufacturerService;
import com.coretex.items.cx_core.ManufacturerItem;
import org.springframework.stereotype.Service;

@Service
public class DefaultManufacturerService extends AbstractGenericItemService<ManufacturerItem> implements ManufacturerService {

	private ManufacturerDao repository;

	public DefaultManufacturerService(ManufacturerDao repository) {
		super(repository);
		this.repository = repository;
	}

}

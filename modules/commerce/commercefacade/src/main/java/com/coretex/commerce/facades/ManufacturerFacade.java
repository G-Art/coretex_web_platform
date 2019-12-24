package com.coretex.commerce.facades;

import com.coretex.commerce.data.minimal.MinimalManufacturerData;
import com.coretex.commerce.data.ProductData;
import com.coretex.items.cx_core.ManufacturerItem;

import java.util.List;

public interface ManufacturerFacade extends PageableDataTableFacade<ManufacturerItem, MinimalManufacturerData> {

	ProductData getByCode(String code);

	Long count();
	List<ProductData> getAll();
}

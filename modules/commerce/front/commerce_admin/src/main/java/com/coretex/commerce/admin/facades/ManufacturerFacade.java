package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.data.minimal.MinimalManufacturerData;
import com.coretex.commerce.admin.data.ProductData;
import com.coretex.items.commerce_core_model.ManufacturerItem;

import java.util.List;

public interface ManufacturerFacade extends PageableDataTableFacade<ManufacturerItem, MinimalManufacturerData> {

	ProductData getByCode(String code);

	Long count();
	List<ProductData> getAll();
}

package com.coretex.commerce.facades;

import com.coretex.commerce.data.minimal.MinimalManufacturerData;
import com.coretex.items.cx_core.ManufacturerItem;

import java.util.List;
import java.util.UUID;

public interface ManufacturerFacade extends PageableDataTableFacade<ManufacturerItem, MinimalManufacturerData> {

	MinimalManufacturerData getByCode(String code);

	Long count();
	List<MinimalManufacturerData> getAll();

	void delete(UUID uuid);
}

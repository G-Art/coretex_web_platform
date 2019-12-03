package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MerchantStoreData;
import com.coretex.commerce.admin.data.MinimalMerchantStoreData;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.List;

public interface StoreFacade extends PageableDataTableFacade<MerchantStoreItem, MinimalMerchantStoreData> {

	MerchantStoreData getByCode(String code);
	List<MerchantStoreData> getAll();
}

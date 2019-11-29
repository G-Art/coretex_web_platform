package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MerchantStoreData;
import com.coretex.commerce.admin.data.MinimalMerchantStoreData;

import java.util.List;

public interface StoreFacade extends PageableDataTableFacade<MinimalMerchantStoreData> {

	MerchantStoreData getByCode(String code);
	List<MerchantStoreData> getAll();
}

package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MerchantStoreData;
import com.coretex.commerce.admin.data.MinimalMerchantStoreData;

import java.util.List;

public interface StoreFacade {

	MerchantStoreData getByCode(String code);
	List<MerchantStoreData> getAll();

	DataTableResults<MinimalMerchantStoreData> tableResult(String draw, long page, Long length);
}

package com.coretex.commerce.facades;

import com.coretex.commerce.data.MerchantStoreData;
import com.coretex.commerce.data.minimal.MinimalMerchantStoreData;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.List;

public interface MerchantStoreFacade extends PageableDataTableFacade<MerchantStoreItem, MinimalMerchantStoreData> {

	MerchantStoreData getByCode(String code);
	List<MerchantStoreData> getAll();
}

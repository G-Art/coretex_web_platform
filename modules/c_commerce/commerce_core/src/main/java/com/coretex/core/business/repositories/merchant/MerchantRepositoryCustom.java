package com.coretex.core.business.repositories.merchant;


import com.coretex.core.model.common.GenericEntityList;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.merchant.MerchantStoreCriteria;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

public interface MerchantRepositoryCustom {

	GenericEntityList<MerchantStoreItem> listByCriteria(MerchantStoreCriteria criteria)
			;

}

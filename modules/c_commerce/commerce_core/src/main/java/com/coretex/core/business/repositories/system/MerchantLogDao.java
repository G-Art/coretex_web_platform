package com.coretex.core.business.repositories.system;

import java.util.List;

import com.coretex.core.activeorm.dao.Dao;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantLogItem;

public interface MerchantLogDao extends Dao<MerchantLogItem> {

	List<MerchantLogItem> findByStore(MerchantStoreItem store);
}

package com.coretex.core.business.repositories.shipping;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.List;

public interface DeliveryServiceDao extends Dao<DeliveryServiceItem> {
	List<? extends DeliveryServiceItem> findActiveForStore(MerchantStoreItem merchantStoreItem);

	<D extends DeliveryServiceItem> D findActiveTypeForCode(String itemType, String code);
}

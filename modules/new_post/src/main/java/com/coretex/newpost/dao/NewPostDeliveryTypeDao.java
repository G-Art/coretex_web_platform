package com.coretex.newpost.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;

import java.util.List;

public interface NewPostDeliveryTypeDao extends Dao<NewPostDeliveryTypeItem> {
	NewPostDeliveryTypeItem findByCode(String code);
}

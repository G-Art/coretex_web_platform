package com.coretex.newpost.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;

public interface NewPostDeliveryTypeDao extends Dao<NewPostDeliveryTypeItem> {
	NewPostDeliveryTypeItem findByCode(String code);
}

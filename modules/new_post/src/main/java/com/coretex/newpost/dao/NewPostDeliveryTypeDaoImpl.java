package com.coretex.newpost.dao;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NewPostDeliveryTypeDaoImpl extends DefaultGenericDao<NewPostDeliveryTypeItem> implements NewPostDeliveryTypeDao {
	public NewPostDeliveryTypeDaoImpl() {
		super(NewPostDeliveryTypeItem.ITEM_TYPE);
	}

	@Override
	public NewPostDeliveryTypeItem findByCode(String code) {
		return findSingle(Map.of("code", code), true);
	}

}

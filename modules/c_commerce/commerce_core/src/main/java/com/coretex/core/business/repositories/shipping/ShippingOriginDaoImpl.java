package com.coretex.core.business.repositories.shipping;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ShippingOriginItem;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ShippingOriginDaoImpl extends DefaultGenericDao<ShippingOriginItem> implements ShippingOriginDao {

	public ShippingOriginDaoImpl() {
		super(ShippingOriginItem.ITEM_TYPE);
	}

	@Override
	public ShippingOriginItem findByStore(UUID storeId) {
		return findSingle(Map.of(ShippingOriginItem.MERCHANT_STORE, storeId), true);
	}
}

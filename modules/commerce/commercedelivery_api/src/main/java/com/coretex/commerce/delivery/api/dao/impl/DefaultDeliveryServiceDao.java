package com.coretex.commerce.delivery.api.dao.impl;

import com.coretex.commerce.delivery.api.dao.DeliveryServiceDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.utils.TypeUtil;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;
import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class DefaultDeliveryServiceDao extends DefaultGenericDao<DeliveryServiceItem> implements DeliveryServiceDao {

	public DefaultDeliveryServiceDao() {
		super(DeliveryServiceItem.ITEM_TYPE);
	}

	@Override
	public <T extends DeliveryServiceItem> T getByUUIDAndType(UUID uuid, Class<T> type) {
		var query = "SELECT * FROM #" + TypeUtil.getMetaTypeCode(type) + " AS ds WHERE ds." + DeliveryServiceItem.UUID + " = :" + DeliveryServiceItem.UUID + "";
		return findSingleByQuery(query, Map.of(DeliveryServiceItem.UUID, uuid));
	}

	@Override
	public <T extends DeliveryTypeItem> T getDeliveryTypeByUUID(UUID uuid) {
		var query = "SELECT * FROM " + DeliveryTypeItem.ITEM_TYPE + " AS dt WHERE dt." + DeliveryTypeItem.UUID + " = :" + DeliveryTypeItem.UUID + "";
		return findSingleByQuery(query, Map.of(DeliveryTypeItem.UUID, uuid));
	}

	@Override
	public <T extends DeliveryServiceItem> T getByCode(String code) {
		var query = "SELECT * FROM " + DeliveryServiceItem.ITEM_TYPE + " AS dt WHERE dt." + DeliveryServiceItem.CODE + " = :" + DeliveryServiceItem.CODE + "";
		return findSingleByQuery(query, Map.of(DeliveryServiceItem.CODE, code));
	}
}

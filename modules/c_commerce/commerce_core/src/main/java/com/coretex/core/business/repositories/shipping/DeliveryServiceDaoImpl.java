package com.coretex.core.business.repositories.shipping;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.relations.commerce_core_model.MerchantStoreDeliveryServiceRelation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeliveryServiceDaoImpl extends DefaultGenericDao<DeliveryServiceItem> implements DeliveryServiceDao {
	public DeliveryServiceDaoImpl() {
		super(DeliveryServiceItem.ITEM_TYPE);
	}

	@Override
	public List<? extends DeliveryServiceItem> findActiveForStore(MerchantStoreItem merchantStoreItem) {
		String query = "SELECT ds.* FROM " + DeliveryServiceItem.ITEM_TYPE + " AS ds "
				+ "JOIN " + MerchantStoreDeliveryServiceRelation.ITEM_TYPE + " AS msdsr ON(msdsr.target = ds.uuid) "
				+ "WHERE ds.active = :active AND msdsr.source = :store";
		return find(query, Map.of("active", true, "store", merchantStoreItem));
	}

	@Override
	public <D extends DeliveryServiceItem> D findActiveTypeForCode(String itemType, String code) {
		String query = "SELECT ds.* FROM " + itemType + " AS ds "
				+ "WHERE ds.active = :active AND ds.code = :code";
		var serviceItems = find(query, Map.of("active", true, "code", code));
		return CollectionUtils.isNotEmpty(serviceItems) ? (D) IterableUtils.get(serviceItems, 0) : null;
	}
}

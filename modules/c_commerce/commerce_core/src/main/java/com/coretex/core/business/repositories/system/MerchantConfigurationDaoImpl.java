package com.coretex.core.business.repositories.system;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class MerchantConfigurationDaoImpl extends DefaultGenericDao<MerchantConfigurationItem> implements MerchantConfigurationDao {
	public MerchantConfigurationDaoImpl() {
		super(MerchantConfigurationItem.ITEM_TYPE);
	}

	@Override
	public List<MerchantConfigurationItem> findByMerchantStore(UUID id) {
		return find(Map.of(MerchantConfigurationItem.MERCHANT_STORE, id));
	}

	@Override
	public MerchantConfigurationItem findByMerchantStoreAndKey(UUID id, String key) {
		return findSingle(Map.of(MerchantConfigurationItem.MERCHANT_STORE, id, MerchantConfigurationItem.KEY, key, MerchantConfigurationItem.ACTIVE, true), true);
	}

}

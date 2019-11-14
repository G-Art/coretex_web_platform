package com.coretex.core.business.repositories.system;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.MerchantLogItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MerchantLogDaoImpl extends DefaultGenericDao<MerchantLogItem> implements MerchantLogDao {

	public MerchantLogDaoImpl() {
		super(MerchantLogItem.ITEM_TYPE);
	}

	@Override
	public List<MerchantLogItem> findByStore(MerchantStoreItem store) {
		return null;
	}
}

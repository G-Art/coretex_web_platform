package com.coretex.core.business.repositories.system;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.enums.commerce_core_model.OptinTypeEnum;
import com.coretex.items.commerce_core_model.OptinItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OptinDaoImpl extends DefaultGenericDao<OptinItem> implements OptinDao {
	public OptinDaoImpl() {
		super(OptinItem.ITEM_TYPE);
	}

	@Override
	public List<OptinItem> findByMerchant(UUID storeId) {
		return null;
	}

	@Override
	public OptinItem findByMerchantAndType(UUID storeId, OptinTypeEnum optinTyle) {
		return null;
	}

	@Override
	public OptinItem findByMerchantAndCode(UUID storeId, String code) {
		return null;
	}
}

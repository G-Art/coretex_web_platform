package com.coretex.core.business.services.system.optin;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OptinItem;
import org.springframework.stereotype.Service;

import com.coretex.core.business.repositories.system.OptinDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.enums.commerce_core_model.OptinTypeEnum;

@Service
public class OptinServiceImpl extends SalesManagerEntityServiceImpl<OptinItem> implements OptinService {


	private OptinDao optinDao;

	public OptinServiceImpl(OptinDao optinDao) {
		super(optinDao);
		this.optinDao = optinDao;
	}


	@Override
	public OptinItem getOptinByCode(MerchantStoreItem store, String code) {
		return optinDao.findByMerchantAndCode(store.getUuid(), code);
	}

	@Override
	public OptinItem getOptinByMerchantAndType(MerchantStoreItem store, OptinTypeEnum type) {
		return optinDao.findByMerchantAndType(store.getUuid(), type);
	}

}

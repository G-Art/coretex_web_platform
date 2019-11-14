package com.coretex.core.business.services.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.coretex.core.business.repositories.system.MerchantLogDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.MerchantLogItem;

@Service("merchantLogService")
public class MerchantLogServiceImpl extends
		SalesManagerEntityServiceImpl<MerchantLogItem> implements
		MerchantLogService {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantLogServiceImpl.class);


	private MerchantLogDao merchantLogDao;

	public MerchantLogServiceImpl(
			MerchantLogDao merchantLogDao) {
		super(merchantLogDao);
		this.merchantLogDao = merchantLogDao;
	}


}

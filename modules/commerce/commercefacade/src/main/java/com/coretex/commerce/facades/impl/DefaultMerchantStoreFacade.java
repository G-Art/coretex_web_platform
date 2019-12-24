package com.coretex.commerce.facades.impl;

import com.coretex.commerce.data.MerchantStoreData;
import com.coretex.commerce.data.minimal.MinimalMerchantStoreData;
import com.coretex.commerce.facades.MerchantStoreFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.MerchantStoreDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalMerchantStoreDataMapper;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultMerchantStoreFacade implements MerchantStoreFacade {

	@Resource
	private MerchantStoreService merchantStoreService;

	@Resource
	private MerchantStoreDataMapper merchantStoreDataMapper;

	@Resource
	private MinimalMerchantStoreDataMapper minimalMerchantStoreDataMapper;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultMerchantStoreFacade.class);

	@Override
	public MerchantStoreData getByCode(String code) {
		return merchantStoreDataMapper.fromItem(merchantStoreService.getByCode(code));
	}

	@Override
	public List<MerchantStoreData> getAll() {
		return merchantStoreService.list()
				.stream()
				.map(merchantStoreDataMapper::fromItem)
				.collect(Collectors.toList());
	}


	@Override
	public PageableService<MerchantStoreItem> getPageableService() {
		return merchantStoreService;
	}

	@Override
	public GenericDataMapper<MerchantStoreItem, MinimalMerchantStoreData> getDataMapper() {
		return minimalMerchantStoreDataMapper;
	}
}

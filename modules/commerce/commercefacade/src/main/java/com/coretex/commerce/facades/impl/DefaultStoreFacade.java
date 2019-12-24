package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.commerce.data.StoreData;
import com.coretex.commerce.data.minimal.MinimalStoreData;
import com.coretex.commerce.facades.StoreFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.StoreDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalStoreDataMapper;
import com.coretex.items.cx_core.StoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Stream;

@Service
public class DefaultStoreFacade implements StoreFacade {

	@Resource
	private StoreService storeService;

	@Resource
	private StoreDataMapper storeDataMapper;

	@Resource
	private MinimalStoreDataMapper minimalStoreDataMapper;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultStoreFacade.class);

	@Override
	public StoreData getByCode(String code) {
		return storeDataMapper.fromItem(storeService.getByCode(code));
	}

	@Override
	public StoreData getByDomain(String domain) {
		return storeDataMapper.fromItem(storeService.getByDomain(domain));
	}

	@Override
	public Stream<StoreData> getAll() {
		return storeService.listReactive()
				.map(storeDataMapper::fromItem);
	}


	@Override
	public PageableService<StoreItem> getPageableService() {
		return storeService;
	}

	@Override
	public GenericDataMapper<StoreItem, MinimalStoreData> getDataMapper() {
		return minimalStoreDataMapper;
	}
}

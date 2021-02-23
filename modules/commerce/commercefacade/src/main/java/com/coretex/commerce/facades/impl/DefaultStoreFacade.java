package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.commerce.data.StoreData;
import com.coretex.commerce.data.minimal.MinimalStoreData;
import com.coretex.commerce.facades.StoreFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.StoreDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalStoreDataMapper;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.items.cx_core.StoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class DefaultStoreFacade implements StoreFacade {

	@Resource
	private StoreService storeService;

	@Resource
	private StoreDataMapper storeDataMapper;

	@Resource
	private MinimalStoreDataMapper minimalStoreDataMapper;

	@Resource
	private ItemService itemService;

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
	public Flux<StoreData> getAll() {
		return storeService.listReactive()
				.map(storeDataMapper::fromItem);
	}

	@Override
	public StoreData getByUuid(UUID uuid) {
		return storeDataMapper.fromItem(storeService.getByUUID(uuid));
	}

	@Override
	public void delete(UUID uuid) {
		storeService.delete(itemService.create(StoreItem.class, uuid));
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

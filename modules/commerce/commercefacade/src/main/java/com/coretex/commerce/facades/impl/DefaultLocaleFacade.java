package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.LocaleService;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.data.LocaleData;
import com.coretex.commerce.data.StoreData;
import com.coretex.commerce.data.minimal.MinimalLocaleData;
import com.coretex.commerce.facades.LocaleFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalLocaleDataMapper;
import com.coretex.items.core.LocaleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class DefaultLocaleFacade implements LocaleFacade {

	@Resource
	private LocaleService storeService;

	@Resource
	private LocaleDataMapper localeDataMapper;

	@Resource
	private MinimalLocaleDataMapper minimalLocaleDataMapper;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultLocaleFacade.class);

	@Override
	public LocaleData getByIso(String iso) {
		return localeDataMapper.fromItem(storeService.getByIso(iso));
	}

	@Override
	public Stream<LocaleData> getByStore(UUID uuid) {
		return storeService.findForStore(uuid).map(localeDataMapper::fromItem);
	}

	@Override
	public Stream<LocaleData> getByStore(StoreData storeData) {
		return storeService.findForStore(storeData.getUuid()).map(localeDataMapper::fromItem);
	}

	@Override
	public Stream<LocaleData> getAll() {
		return storeService.listReactive()
				.map(localeDataMapper::fromItem);
	}

	@Override
	public PageableService<LocaleItem> getPageableService() {
		return storeService;
	}

	@Override
	public GenericDataMapper<LocaleItem, MinimalLocaleData> getDataMapper() {
		return minimalLocaleDataMapper;
	}
}

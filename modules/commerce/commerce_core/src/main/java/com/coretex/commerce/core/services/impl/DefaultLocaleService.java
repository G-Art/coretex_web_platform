package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.CustomLocaleDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.LocaleService;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.StoreItem;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
public class DefaultLocaleService extends AbstractGenericItemService<LocaleItem> implements LocaleService {

	private CustomLocaleDao customLocaleDao;

	public DefaultLocaleService(CustomLocaleDao customLocaleDao) {
		super(customLocaleDao);
		this.customLocaleDao = customLocaleDao;
	}

	@Override
	public LocaleItem getByIso(String iso) {
		return customLocaleDao.findByIso(iso);
	}

	@Override
	public Stream<LocaleItem> findForStore(UUID uuid) {
		return customLocaleDao.findForStore(uuid);
	}

	@Override
	public Stream<LocaleItem> findForStore(StoreItem store) {
		return findForStore(store.getUuid());
	}
}

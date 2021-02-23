package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.LocaleDao;
import com.coretex.items.core.LocaleItem;
import reactor.core.publisher.Flux;

import java.util.UUID;

public abstract class CustomLocaleDao extends LocaleDao {
	public abstract LocaleItem findByIso(String iso);

	public abstract Flux<LocaleItem> findForStore(UUID uuid);
}

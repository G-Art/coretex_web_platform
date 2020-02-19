package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.LocaleDao;
import com.coretex.items.core.LocaleItem;

import java.util.UUID;
import java.util.stream.Stream;

public abstract class CustomLocaleDao extends LocaleDao {
	public abstract LocaleItem findByIso(String iso);

	public abstract Stream<LocaleItem> findForStore(UUID uuid);
}

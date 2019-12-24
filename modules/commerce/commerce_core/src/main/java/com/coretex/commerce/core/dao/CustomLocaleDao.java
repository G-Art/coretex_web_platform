package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.core.LocaleItem;

import java.util.UUID;
import java.util.stream.Stream;

public interface CustomLocaleDao extends Dao<LocaleItem> {
	LocaleItem findByIso(String iso);

	Stream<LocaleItem> findForStore(UUID uuid);
}

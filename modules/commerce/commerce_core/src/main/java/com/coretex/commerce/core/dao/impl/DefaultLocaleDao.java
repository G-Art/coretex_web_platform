package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.CustomLocaleDao;
import com.coretex.core.activeorm.dao.LocaleDao;
import com.coretex.items.core.LocaleItem;
import com.coretex.relations.cx_core.StoreLocaleRelation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class DefaultLocaleDao extends LocaleDao implements CustomLocaleDao {


	@Override
	public LocaleItem findByIso(String iso) {
		return findSingle(Map.of(LocaleItem.ISO, iso), true);
	}

	@Override
	public Stream<LocaleItem> findForStore(UUID uuid) {
		return findReactive("SELECT l.* FROM " + LocaleItem.ITEM_TYPE + " as l " +
						"LEFT JOIN " + StoreLocaleRelation.ITEM_TYPE + " as slr " +
						"ON (slr." + StoreLocaleRelation.TARGET + " = l."+LocaleItem.UUID+") " +
						"WHERE slr." + StoreLocaleRelation.SOURCE + " = :storeUuid",
				Map.of("storeUuid", uuid));
	}
}

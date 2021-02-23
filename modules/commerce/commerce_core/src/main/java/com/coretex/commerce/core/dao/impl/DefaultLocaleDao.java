package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.CustomLocaleDao;
import com.coretex.items.core.LocaleItem;
import com.coretex.relations.cx_core.StoreLocaleRelation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class DefaultLocaleDao extends CustomLocaleDao {

	@Override
	public LocaleItem findByIso(String iso) {
		return findOne(Map.of(LocaleItem.ISO, iso), true).orElseThrow(NoSuchElementException::new);
	}

	@Override
	public Flux<LocaleItem> findForStore(UUID uuid) {
		return findReactive("SELECT l.* FROM " + LocaleItem.ITEM_TYPE + " as l " +
						"LEFT JOIN " + StoreLocaleRelation.ITEM_TYPE + " as slr " +
						"ON (slr." + StoreLocaleRelation.TARGET + " = l."+LocaleItem.UUID+") " +
						"WHERE slr." + StoreLocaleRelation.SOURCE + " = :storeUuid",
				Map.of("storeUuid", uuid));
	}
}

package com.coretex.core.business.repositories.reference.country;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.relations.core.LocaleCountryRelation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CountryDaoImp extends DefaultGenericDao<CountryItem> implements CountryDao {

	public CountryDaoImp() {
		super(CountryItem.ITEM_TYPE);
	}

	@Override
	public CountryItem findByIsoCode(String code) {
		return findSingle(Map.of(CountryItem.ISO_CODE, code), true);
	}

	@Override
	public List<CountryItem> listByLanguage(LanguageItem languageItem) {
		String query = "SELECT c.* FROM " + CountryItem.ITEM_TYPE + " AS c " +
				"LEFT JOIN " + LocaleCountryRelation.ITEM_TYPE + " AS lcr ON (c.uuid = lcr.target) " +
				"LEFT JOIN " + LocaleItem.ITEM_TYPE + " AS l ON (lcr.source = l.uuid) " +
				"WHERE l." + LocaleItem.ISO + " = :code ";
		return find(query, Map.of("code", languageItem.getCode()));
	}

	@Override
	public List<CountryItem> listCountryZonesByLanguage(UUID id) {
		return null;
	}
}

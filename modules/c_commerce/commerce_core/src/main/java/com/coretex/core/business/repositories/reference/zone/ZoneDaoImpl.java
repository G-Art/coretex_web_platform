package com.coretex.core.business.repositories.reference.zone;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.ZoneItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.relations.commerce_core_model.ZoneCountryRelation;
import com.coretex.relations.core.LocaleCountryRelation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ZoneDaoImpl extends DefaultGenericDao<ZoneItem> implements ZoneDao {

	public ZoneDaoImpl() {
		super(ZoneItem.ITEM_TYPE);
	}

	@Override
	public ZoneItem findByCode(String code) {
		return findSingle(Map.of(ZoneItem.CODE, code), true);
	}

	@Override
	public List<ZoneItem> listByLanguage(UUID id) {
		String query = "SELECT z.* FROM " + ZoneItem.ITEM_TYPE + " AS z " +
				"JOIN " + ZoneCountryRelation.ITEM_TYPE + " AS zcr ON (zcr.source = z.uuid) " +
				"JOIN " + CountryItem.ITEM_TYPE + " AS zc ON (zcr.target = zc.uuid) " +
				"JOIN " + LocaleCountryRelation.ITEM_TYPE + " AS lcr ON (lcr.target = zc.uuid) " +
				"JOIN " + LocaleItem.ITEM_TYPE + " AS l ON (lcr.source = l.uuid) " +
				"WHERE l.uuid=:localeId";
		return find(query, Map.of("localeId", id));
	}

	@Override
	public List<ZoneItem> listByLanguageAndCountry(String isoCode, UUID languageId) {
		String query = "SELECT z.* FROM " + ZoneItem.ITEM_TYPE + " AS z " +
				"JOIN " + ZoneCountryRelation.ITEM_TYPE + " AS zcr ON (zcr.source = z.uuid) " +
				"JOIN " + CountryItem.ITEM_TYPE + " AS zc ON (zcr.target = zc.uuid) " +
				"WHERE zc.isoCode=:isoCode";
		return find(query, Map.of("isoCode", isoCode));
	}
}

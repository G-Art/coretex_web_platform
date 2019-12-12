package com.coretex.core.business.repositories.reference.zone;

import java.util.List;
import java.util.UUID;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.ZoneItem;

public interface ZoneDao extends Dao<ZoneItem> {


	ZoneItem findByCode(String code);

	//	@Query("select z from ZoneItem z left join fetch z.descriptions z    d where zd.language.id=?1")
	List<ZoneItem> listByLanguage(UUID id);

	//	@Query("select z from ZoneItem z left join fetch z.descriptions zd join fetch z.country zc where zc.isoCode=?1 and zd.language.id=?2")
	List<ZoneItem> listByLanguageAndCountry(String isoCode, UUID languageId);

}

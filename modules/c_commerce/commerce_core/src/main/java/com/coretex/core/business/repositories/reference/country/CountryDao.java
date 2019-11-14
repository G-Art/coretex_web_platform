package com.coretex.core.business.repositories.reference.country;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.core.CountryItem;
import com.coretex.core.activeorm.dao.Dao;


public interface CountryDao extends Dao<CountryItem> {

	//	@Query("select c from CountryItem c left join fetch c.descriptions cd where c.isoCode=?1")
	CountryItem findByIsoCode(String code);


	//	@Query("select c from CountryItem c left join fetch c.descriptions cd where cd.language.id=?1")
	List<CountryItem> listByLanguage(LanguageItem id);

	/**
	 * get country including zones by language
	 *
	 * @param id
	 **/
//	@Query("select distinct c from CountryItem c left join fetch c.descriptions cd left join fetch c.zones cz left join fetch cz.descriptions where cd.language.id=?1")
	List<CountryItem> listCountryZonesByLanguage(UUID id);

}

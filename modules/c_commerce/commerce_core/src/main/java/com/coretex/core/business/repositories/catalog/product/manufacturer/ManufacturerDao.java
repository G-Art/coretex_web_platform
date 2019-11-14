package com.coretex.core.business.repositories.catalog.product.manufacturer;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.core.activeorm.dao.Dao;

public interface ManufacturerDao extends Dao<ManufacturerItem> {

	//	@Query("select count(distinct p) from ProductItem as p where p.manufacturer.id=?1")
	Long countByProduct(UUID manufacturerId);

	//	@Query("select m from ManufacturerItem m left join m.descriptions md join fetch m.merchantStore ms where ms.id=?1 and md.language.id=?2")
	List<ManufacturerItem> findByStoreAndLanguage(UUID storeId, UUID languageId);

	//	@Query("select m from ManufacturerItem m left join m.descriptions md join fetch m.merchantStore ms where m.id=?1")
	ManufacturerItem findOne(UUID id);

	//	@Query("select m from ManufacturerItem m left join m.descriptions md join fetch m.merchantStore ms where ms.id=?1")
	List<ManufacturerItem> findByStore(UUID storeId);

	//	@Query("select distinct manufacturer from ProductItem as p join p.manufacturer manufacturer join manufacturer.descriptions md join p.categories categs where categs.id in (?1) and md.language.id=?2")
	List<ManufacturerItem> findByCategoriesAndLanguage(List<UUID> categoryIds, UUID languageId);

	//	@Query("select m from ManufacturerItem m left join m.descriptions md join fetch m.merchantStore ms where m.code=?1 and ms.id=?2")
	ManufacturerItem findByCodeAndMerchandStore(String code, UUID storeId);
}

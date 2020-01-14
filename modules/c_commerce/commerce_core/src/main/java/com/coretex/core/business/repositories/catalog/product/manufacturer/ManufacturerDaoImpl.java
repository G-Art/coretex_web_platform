package com.coretex.core.business.repositories.catalog.product.manufacturer;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.relations.cx_core.CategoryProductRelation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ManufacturerDaoImpl extends DefaultGenericDao<ManufacturerItem> implements ManufacturerDao {
	public ManufacturerDaoImpl() {
		super(ManufacturerItem.ITEM_TYPE);
	}

	@Override
	public Long countByProduct(UUID manufacturerId) {
		return null;
	}

	@Override
	public List<ManufacturerItem> findByStoreAndLanguage(UUID storeId, UUID languageId) {
		return find(Map.of(ManufacturerItem.STORE, storeId));
	}

	@Override
	public ManufacturerItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public List<ManufacturerItem> findByStore(UUID storeId) {
		return find(Map.of(ManufacturerItem.STORE, storeId));
	}

	//	@Query("select distinct manufacturer from ProductItem as p join p.manufacturer manufacturer join manufacturer.descriptions md join p.categories categs where categs.id in (?1) and md.language.id=?2")
	@Override
	public List<ManufacturerItem> findByCategoriesAndLanguage(List<UUID> categoryIds, UUID languageId) {

		String qs = "SELECT manufacturer.* FROM " + ProductItem.ITEM_TYPE + " AS p " +
				"JOIN " + CategoryProductRelation.ITEM_TYPE + " AS categories ON (categories.target = p.uuid)" +
				"JOIN " + ManufacturerItem.ITEM_TYPE + " AS manufacturer ON (p.manufacturer = manufacturer.uuid)" +
				"WHERE categories.source IN (:cid) ";

		return getSearchService().<ManufacturerItem>search(qs, Map.of("cid", categoryIds)).getResult();

	}

	@Override
	public ManufacturerItem findByCodeAndMerchandStore(String code, UUID storeId) {
		return findSingle(Map.of(ManufacturerItem.CODE, code, ManufacturerItem.STORE, storeId), true);
	}
}

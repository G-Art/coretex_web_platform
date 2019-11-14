package com.coretex.core.business.repositories.catalog.product.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.ProductOptionItem;
import com.coretex.core.activeorm.dao.Dao;

public interface ProductOptionDao extends Dao<ProductOptionItem> {

	//	@Query("select p from ProductOptionItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where p.id = ?1")
	ProductOptionItem findOne(UUID id);

	//	@Query("select p from ProductOptionItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where p.id = ?2 and pm.id = ?1")
	ProductOptionItem findOne(UUID storeId, UUID id);

	//	@Query("select distinct p from ProductOptionItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where pm.id = ?1 and pd.language.id = ?2")
	List<ProductOptionItem> findByStoreId(UUID storeId, UUID languageId);

	//	@Query("select p from ProductOptionItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where pm.id = ?1 and pd.name like %?2% and pd.language.id = ?3")
	List<ProductOptionItem> findByName(UUID storeId, String name, UUID languageId);

	//	@Query("select p from ProductOptionItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where pm.id = ?1 and p.code = ?2")
	ProductOptionItem findByCode(UUID storeId, String optionCode);

	//	@Query("select distinct p from ProductOptionItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where pm.id = ?1 and p.code = ?2 and p.readOnly = ?3")
	List<ProductOptionItem> findByReadOnly(UUID storeId, UUID languageId, boolean readOnly);


}

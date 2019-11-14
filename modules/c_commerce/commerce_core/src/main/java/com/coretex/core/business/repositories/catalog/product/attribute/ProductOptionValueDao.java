package com.coretex.core.business.repositories.catalog.product.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.ProductOptionValueItem;
import com.coretex.core.activeorm.dao.Dao;

public interface ProductOptionValueDao extends Dao<ProductOptionValueItem> {

	//	@Query("select p from ProductOptionValueItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where p.id = ?1")
	ProductOptionValueItem findOne(UUID id);

	//	@Query("select p from ProductOptionValueItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where p.id = ?2  and pm.id = ?1")
	ProductOptionValueItem findOne(UUID storeId, UUID id);

	//	@Query("select distinct p from ProductOptionValueItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where pm.id = ?1 and pd.language.id = ?2")
	List<ProductOptionValueItem> findByStoreId(UUID storeId, UUID languageId);

	//	@Query("select p from ProductOptionValueItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where pm.id = ?1 and p.code = ?2")
	ProductOptionValueItem findByCode(UUID storeId, String optionValueCode);

	//	@Query("select p from ProductOptionValueItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where pm.id = ?1 and pd.name like %?2% and pd.language.id = ?3")
	List<ProductOptionValueItem> findByName(UUID storeId, String name, UUID languageId);


	//	@Query("select distinct p from ProductOptionValueItem p join fetch p.merchantStore pm left join fetch p.descriptions pd where pm.id = ?1 and pd.language.id = ?2 and p.productOptionDisplayOnly = ?3")
	List<ProductOptionValueItem> findByReadOnly(UUID storeId, UUID languageId, boolean readOnly);


}

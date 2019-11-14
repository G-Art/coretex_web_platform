package com.coretex.core.business.repositories.merchant;

import com.coretex.core.activeorm.dao.Dao;

import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.UUID;

public interface MerchantDao extends Dao<MerchantStoreItem>, MerchantRepositoryCustom {

	//	@Query("select m from MerchantStoreItem m left join fetch m.country mc left join fetch m.currency mc left join fetch m.zone mz left join fetch m.defaultLanguage md left join fetch m.languages mls where m.code = ?1")
	MerchantStoreItem findByCode(String code);

	//	@Query("select m from MerchantStoreItem m left join fetch m.country mc left join fetch m.currency mc left join fetch m.zone mz left join fetch m.defaultLanguage md left join fetch m.languages mls where m.id = ?1")
	MerchantStoreItem getById(UUID id);

	//	@Query("SELECT COUNT(m) > 0 FROM MerchantStoreItem m WHERE m.code = :code")
	boolean existsByCode(String code);
}

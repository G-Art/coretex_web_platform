package com.coretex.core.business.repositories.content;

import java.util.List;
import java.util.UUID;

import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.core.activeorm.dao.Dao;

public interface ContentDao extends Dao<ContentItem>, ContentRepositoryCustom {


	//	@Query("select c from ContentItem c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.contentType = ?1 and cm.id = ?2 and cd.language.id = ?3 order by c.sortOrder asc")
	List<ContentItem> findByType(ContentTypeEnum contentType, UUID storeId, UUID languageId);

	//	@Query("select c from ContentItem c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.contentType = ?1 and cm.id = ?2 order by c.sortOrder asc")
	List<ContentItem> findByType(ContentTypeEnum contentType, UUID storeId);

	//	@Query("select c from ContentItem c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.contentType in (?1) and cm.id = ?2 and cd.language.id = ?3 order by c.sortOrder asc")
	List<ContentItem> findByTypes(List<ContentTypeEnum> contentTypes, UUID storeId, UUID languageId);

	//	@Query("select c from ContentItem c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.contentType in (?1) and cm.id = ?2 order by c.sortOrder asc")
	List<ContentItem> findByTypes(List<ContentTypeEnum> contentTypes, UUID storeId);

	//	@Query("select c from ContentItem c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.code = ?1 and cm.id = ?2")
	ContentItem findByCode(String code, UUID storeId);

	//	@Query("select c from ContentItem c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.contentType = ?1 and cm.id=?3 and c.code like ?2 and cd.language.id = ?4")
	List<ContentItem> findByCodeLike(ContentTypeEnum contentType, String code, UUID storeId, UUID languageId);

	//	@Query("select c from ContentItem c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.code = ?1 and cm.id = ?2 and cd.language.id = ?3")
	ContentItem findByCode(String code, UUID storeId, UUID languageId);

	//	@Query("select c from ContentItem c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.id = ?1 and cd.language.id = ?2")
	ContentItem findByIdAndLanguage(UUID contentId, UUID languageId);

	//	@Query("select c from ContentItem c left join fetch c.descriptions cd join fetch c.merchantStore cm where c.id = ?1")
	ContentItem findOne(UUID contentId);


}

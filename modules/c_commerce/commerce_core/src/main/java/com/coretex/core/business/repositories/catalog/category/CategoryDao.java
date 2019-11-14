package com.coretex.core.business.repositories.catalog.category;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.core.activeorm.dao.Dao;


public interface CategoryDao extends Dao<CategoryItem>, CategoryRepositoryCustom {


	//	@Query("select c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cd.seUrl like ?2 and cm.id = ?1 order by c.sortOrder asc")
	List<CategoryItem> listByFriendlyUrl(UUID storeId, String friendlyUrl);

	//	@Query("select c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cd.seUrl=?2 and cm.id = ?1")
	CategoryItem findByFriendlyUrl(UUID storeId, String friendlyUrl);

	//	@Query("select c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cd.name like %?2% and cdl.id=?3 and cm.id = ?1 order by c.sortOrder asc")
	List<CategoryItem> findByName(UUID storeId, String name, UUID languageId);

	//	@Query("select c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where c.code=?2 and cm.id = ?1")
	CategoryItem findByCode(UUID storeId, String code);

	//	@Query("select c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where c.code in (?2) and cdl.id=?3 and cm.id = ?1 order by c.sortOrder asc")
	List<CategoryItem> findByCodes(UUID storeId, List<String> codes, UUID languageId);

	//	@Query("select c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where c.id in (?2) and cdl.id=?3 and cm.id = ?1 order by c.sortOrder asc")
	List<CategoryItem> findByIds(UUID storeId, List<UUID> ids, UUID languageId);

	//	@Query("select c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cdl.id=?2 and c.id = ?1")
	CategoryItem findById(UUID categoryId, UUID languageId);

	//	@Query("select c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cm.code=?1 and c.code=?2")
	CategoryItem findByCode(String merchantStoreCode, String code);

	//	@Query("select c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where c.id=?1")
	CategoryItem findOne(UUID categoryId);

	//	@Query("select distinct c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cm.id=?1 and c.lineage like %?2% order by c.lineage, c.sortOrder asc")
	List<CategoryItem> findByLineage(UUID merchantId, String linenage);

	//	@Query("select distinct c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cm.code= ?1 and c.lineage like %?2% order by c.lineage, c.sortOrder asc")
	List<CategoryItem> findByLineage(String storeCode, String linenage);

	//	@Query("select distinct c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cm.id=?1 and c.depth >= ?2 order by c.lineage, c.sortOrder asc")
	List<CategoryItem> findByDepth(UUID merchantId, int depth);

	//	@Query("select distinct c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cm.id=?1 and cdl.id=?3 and c.depth >= ?2 order by c.lineage, c.sortOrder asc")
	List<CategoryItem> findByDepth(UUID merchantId, int depth, UUID languageId);

	//	@Query("select distinct c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cm.id=?1 and cdl.id=?3 and c.depth >= ?2 and c.featured=true order by c.lineage, c.sortOrder asc")
	List<CategoryItem> findByDepthFilterByFeatured(UUID merchantId, int depth, UUID languageId);

	//	@Query("select distinct c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm left join fetch c.parent cp where cp.id=?1 and cdl.id=?2 order by c.lineage, c.sortOrder asc")
	List<CategoryItem> findByParent(UUID parentId, UUID languageId);

	//	@Query("select distinct c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cm.id=?1 and cdl.id=?2 order by c.lineage, c.sortOrder asc")
	List<CategoryItem> findByStore(UUID merchantId, UUID languageId);

	//	@Query("select distinct c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cm.id=?1 order by c.lineage, c.sortOrder asc")
	List<CategoryItem> findByStore(UUID merchantId);


}

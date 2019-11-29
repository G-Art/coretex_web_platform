package com.coretex.core.business.repositories.catalog.category;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.activeorm.exceptions.AmbiguousResultException;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.relations.commerce_core_model.CategoryCategoryRelation;
import com.coretex.relations.commerce_core_model.CategoryProductRelation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class CategoryDaoImpl extends DefaultGenericDao<CategoryItem> implements CategoryDao {

	public CategoryDaoImpl() {
		super(CategoryItem.ITEM_TYPE);
	}

	@Override
	public List<Map<String, Object>> countProductsByCategories(MerchantStoreItem store, List<UUID> categoryIds) {


		String qs = "SELECT categories.source, count(product.uuid) FROM " + ProductItem.ITEM_TYPE + " AS product " +
				"JOIN " + CategoryProductRelation.ITEM_TYPE + " AS categories ON (categories.target = product.uuid)" +
				"WHERE categories.source IN (:cid) " +
				"AND product.available=:availability " +
				"AND (product.dateAvailable IS NULL OR product.dateAvailable<=:dt) " +
				"GROUP BY categories.source";

		return getSearchService().<Map<String, Object>>search(qs, Map.of("cid", categoryIds, "dt", new Date(), "availability", true)).getResult();

	}



	@SuppressWarnings("unchecked")
	@Override
	public List<CategoryItem> listByStoreAndParent(MerchantStoreItem store, CategoryItem category) {

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select c from CategoryItem c join fetch c.merchantStore cm ");

		if (store == null) {
			if (category == null) {
				//query.from(qCategory)
				queryBuilder.append(" where c.parent IsNull ");
				//.where(qCategory.parent.isNull())
				//.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			} else {
				//query.from(qCategory)
				queryBuilder.append(" join fetch c.parent cp where cp.id =:cid ");
				//.where(qCategory.parent.eq(category))
				//.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			}
		} else {
			if (category == null) {
				//query.from(qCategory)
				queryBuilder.append(" where c.parent IsNull and cm.id=:mid ");
				//.where(qCategory.parent.isNull()
				//	.and(qCategory.merchantStore.eq(store)))
				//.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			} else {
				//query.from(qCategory)
				queryBuilder.append(" join fetch c.parent cp where cp.id =:cId and cm.id=:mid ");
				//.where(qCategory.parent.eq(category)
				//	.and(qCategory.merchantStore.eq(store)))
				//.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			}
		}

		queryBuilder.append(" order by c.sortOrder asc");

		String hql = queryBuilder.toString();
//		Query q = this.em.createQuery(hql);
//
//    	q.setParameter("cid", category.getId());
//    	if (store != null) {
//    		q.setParameter("mid", store.getId());
//    	}


//		return q.getResultList();
		return null;
	}

	@Override
	public List<CategoryItem> listByFriendlyUrl(UUID storeId, String friendlyUrl) {
		return null;
	}

	@Override
	public CategoryItem findByFriendlyUrl(UUID storeId, String friendlyUrl) {
		return findSingle(Map.of(CategoryItem.MERCHANT_STORE, storeId, CategoryItem.SE_URL, friendlyUrl), true);
	}

	@Override
	public List<CategoryItem> findByName(UUID storeId, String name, UUID languageId) {
		return null;
	}

	@Override
	public CategoryItem findByCode(UUID storeId, String code) {
		return findSingle(Map.of(CategoryItem.MERCHANT_STORE, storeId, CategoryItem.CODE, code), true);
	}

	@Override
	public List<CategoryItem> findByCodes(UUID storeId, List<String> codes, UUID languageId) {
		String stringBuilder = "Select * from \"" + CategoryItem.ITEM_TYPE + "\" as c" +
				" WHERE c." + CategoryItem.MERCHANT_STORE + " = :" + CategoryItem.MERCHANT_STORE +
				" AND c.code IN (:" + CategoryItem.CODE + "S) order by c." + CategoryItem.SORT_ORDER + " asc";
		return getSearchService().<CategoryItem>search(stringBuilder, Map.of(CategoryItem.MERCHANT_STORE, storeId, CategoryItem.CODE+"S", codes)).getResult();
	}

	@Override
	public List<CategoryItem> findByIds(UUID storeId, List<UUID> ids, UUID languageId) {
		String stringBuilder = "Select * from \"" + CategoryItem.ITEM_TYPE + "\" as c" +
				" WHERE c." + CategoryItem.MERCHANT_STORE + " = :" + CategoryItem.MERCHANT_STORE +
				" AND c.uuid IN (:" + CategoryItem.UUID + "S) order by c." + CategoryItem.SORT_ORDER + " asc";
		return getSearchService().<CategoryItem>search(stringBuilder, Map.of(CategoryItem.MERCHANT_STORE, storeId, CategoryItem.UUID+"S", ids)).getResult();
	}

	@Override
	public CategoryItem findById(UUID categoryId, UUID languageId) {
		return find(categoryId);
	}

	@Override
	public CategoryItem findByCode(String merchantStoreCode, String code) {
		String query = "SELECT DISTINCT c.* FROM "+CategoryItem.ITEM_TYPE+" AS c " +
				" LEFT JOIN " + MerchantStoreItem.ITEM_TYPE + " AS ms ON (ms.uuid = c.merchantStore) " +
				" WHERE ms.code = :mcode and c.code = :code order by c.lineage, c.sortOrder asc";
		var result = getSearchService().<CategoryItem>search(query, Map.of("mcode", merchantStoreCode, "code", code)).getResult();

		if(CollectionUtils.isNotEmpty(result) && result.size()>1){
			throw new AmbiguousResultException(String.format("Result contain more than one result (result count: %s)", result.size()));
		}
		return CollectionUtils.isNotEmpty(result) ? result.iterator().next() : null;
	}

	@Override
	public CategoryItem findOne(UUID categoryId) {
		return find(categoryId);
	}

	@Override
	public List<CategoryItem> findByLineage(UUID merchantId, String linenage) {
		String query = "SELECT DISTINCT c.* FROM "+CategoryItem.ITEM_TYPE+" AS c " +
				" WHERE c.merchantStore = :mid AND c.lineage LIKE :linenage ORDER BY c.lineage, c.sortOrder ASC";
		return getSearchService().<CategoryItem>search(query, Map.of("mid", merchantId, "linenage", "%"+linenage+"%")).getResult();
	}

	@Override
	public List<CategoryItem> findByLineage(String storeCode, String linenage) {
		String query = "SELECT DISTINCT c.* FROM "+CategoryItem.ITEM_TYPE+" AS c " +
				" LEFT JOIN " + MerchantStoreItem.ITEM_TYPE + " AS ms ON (ms.uuid = c.merchantStore) " +
				" WHERE ms.code = :mcode AND c.lineage LIKE :linenage ORDER BY c.lineage, c.sortOrder ASC";
		return getSearchService().<CategoryItem>search(query, Map.of("mcode", storeCode, "linenage", "%"+linenage+"%")).getResult();
	}

	@Override
	public List<CategoryItem> findByDepth(UUID merchantId, int depth) {
		return findByDepth(merchantId, depth, null);
	}

	@Override
	public List<CategoryItem> findByDepth(UUID merchantId, int depth, UUID languageId) {
		String stringBuilder = "select * FROM \"" + CategoryItem.ITEM_TYPE + "\" AS c" +
				" WHERE c." + CategoryItem.MERCHANT_STORE + " = :" + CategoryItem.MERCHANT_STORE +
				" AND c." + CategoryItem.DEPTH + " >= :" + CategoryItem.DEPTH + " ORDER BY c." + CategoryItem.SORT_ORDER + " ASC";
		return getSearchService().<CategoryItem>search(stringBuilder, Map.of(CategoryItem.MERCHANT_STORE, merchantId, CategoryItem.DEPTH, depth)).getResult();
	}

	//	@Query("select distinct c from CategoryItem c left join fetch c.descriptions cd join fetch cd.language cdl join fetch c.merchantStore cm where cm.id=?1 and cdl.id=?3 and c.depth >= ?2 and c.featured=true order by c.lineage, c.sortOrder asc")
	@Override
	public List<CategoryItem> findByDepthFilterByFeatured(UUID merchantId, int depth, UUID languageId) {
		String stringBuilder = "Select * from \"" + CategoryItem.ITEM_TYPE + "\" as c" +
				" WHERE c." + CategoryItem.MERCHANT_STORE + " = :" + CategoryItem.MERCHANT_STORE +
				" AND c." + CategoryItem.DEPTH + " >= :" + CategoryItem.DEPTH + " and c." + CategoryItem.FEATURED + "=true" + " order by c." + CategoryItem.SORT_ORDER + " asc";
		return getSearchService().<CategoryItem>search(stringBuilder, Map.of(CategoryItem.MERCHANT_STORE, merchantId, CategoryItem.DEPTH, depth)).getResult();
	}

	@Override
	public List<CategoryItem> findByParent(UUID parentId) {

		String qs = "SELECT * FROM " + CategoryItem.ITEM_TYPE + " AS category ";

		if(Objects.isNull(parentId)){
			qs = qs + "LEFT JOIN " + CategoryCategoryRelation.ITEM_TYPE + " AS categories ON (categories.target = category.uuid) " +
					"WHERE categories.target IS NULL";
		}else{
			qs = qs + "LEFT JOIN " + CategoryCategoryRelation.ITEM_TYPE + " AS categories ON (categories.target = category.uuid) " +
					"WHERE categories.source = :cid";
		}
		if(Objects.isNull(parentId)){
			return getSearchService().<CategoryItem>search(qs).getResult();
		}
		return getSearchService().<CategoryItem>search(qs, Map.of("cid", parentId)).getResult();
	}

	@Override
	public List<CategoryItem> findByStore(UUID merchantId, UUID languageId) {
		return find(Map.of(CategoryItem.MERCHANT_STORE, merchantId));
	}

	@Override
	public List<CategoryItem> findByStore(UUID merchantId) {
		return find(Map.of(CategoryItem.MERCHANT_STORE, merchantId));
	}
}

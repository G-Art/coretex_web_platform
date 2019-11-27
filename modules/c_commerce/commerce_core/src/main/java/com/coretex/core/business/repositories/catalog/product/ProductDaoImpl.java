package com.coretex.core.business.repositories.catalog.product;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.activeorm.exceptions.AmbiguousResultException;
import com.coretex.core.business.constants.Constants;
import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.core.model.catalog.product.ProductList;
import com.coretex.core.model.catalog.product.attribute.AttributeCriteria;
import com.coretex.enums.commerce_core_model.RentalStatusEnum;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.relations.commerce_core_model.CategoryProductRelation;
import com.coretex.relations.commerce_core_model.ProductProductAttributeRelation;
import com.coretex.relations.commerce_core_model.ProductProductAvailabilityRelation;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class ProductDaoImpl extends DefaultGenericDao<ProductItem> implements ProductDao {

	public ProductDaoImpl() {
		super(ProductItem.ITEM_TYPE);
	}

	@Override
	public ProductItem getByCode(String productCode, LocaleItem language) {
		return findSingle(Map.of(ProductItem.SKU, productCode), true);
	}

	@Override
	public ProductItem getByCode(String productCode) {
		return findSingle(Map.of(ProductItem.SKU, productCode), true);
	}

	public ProductItem getByFriendlyUrl(MerchantStoreItem store, String seUrl, Locale locale) {

		List regionList = List.of("*", locale.getCountry());

		String qs = "SELECT DISTINCT pattr.productOptionSortOrder, p.* FROM " + ProductItem.ITEM_TYPE + " AS p " +
				"JOIN " + ProductProductAvailabilityRelation.ITEM_TYPE + " AS ppar ON (ppar.source = p.uuid) " +
				"JOIN " + ProductAvailabilityItem.ITEM_TYPE + " AS pa ON (ppar.target = pa.uuid) " +
				"LEFT JOIN " + ProductProductAttributeRelation.ITEM_TYPE + " AS ppattrr ON (ppattrr.source = p.uuid) " +
				"LEFT JOIN " + ProductAttributeItem.ITEM_TYPE + " AS pattr ON (ppattrr.target = pattr.uuid) " +
				"WHERE pa.region IN (:lid) " +
				"AND p.seUrl = :seUrl " +
				"AND (p.available IS NULL OR p.available=:available)" +
				"AND (p.dateAvailable IS NULL OR p.dateAvailable <= :dt) " +
				"ORDER BY pattr.productOptionSortOrder ";

		var result = getSearchService().<ProductItem>search(qs, Map.of("lid", regionList, "dt", new Date(), "seUrl", seUrl, "available", Boolean.TRUE)).getResult();
		if (CollectionUtils.isEmpty(result)) {
			return null;
		}
		if (result.size() > 1) {
			throw new AmbiguousResultException(String.format("Result contain more than one result (result count: %s)", result.size()));
		}
		return result.iterator().next();
	}

	@Override
	public List<ProductItem> getProductsForLocale(MerchantStoreItem store, Set<UUID> categoryIds, LocaleItem language, Locale locale) {

		ProductList products = this.getProductsListForLocale(store, categoryIds, language, locale, 0, -1);

		return products.getProducts();
	}

	@Override
	public ProductItem getProductForLocale(UUID productId, LocaleItem language, Locale locale) {


		List regionList = new ArrayList();
		regionList.add("*");
		regionList.add(locale.getCountry());


		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from ProductItem as p ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.merchantStore pm ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");


		//images
		qs.append("left join fetch p.images images ");
		//options
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");
		qs.append("left join fetch p.relationships pr ");
		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");
		qs.append("left join fetch p.taxClass tx ");

		//RENTAL
		qs.append("left join fetch p.owner owner ");

		qs.append("where p.id=:pid and pa.region in (:lid) ");
		qs.append("and pd.language.id=:lang and papd.language.id=:lang ");
		qs.append("and p.available=true and p.dateAvailable<=:dt ");
		//this cannot be done on child elements from left join
		//qs.append("and pod.languageId=:lang and povd.languageId=:lang");

		String hql = qs.toString();
//				Query q = this.em.createQuery(hql);
//
//		    	q.setParameter("pid", productId);
//		    	q.setParameter("lid", regionList);
//		    	q.setParameter("dt", new Date());
//		    	q.setParameter("lang", language.getId());
//
//		        @SuppressWarnings("unchecked")
//				List<ProductItem> results = q.getResultList();
//		        if (results.isEmpty()) return null;
//		        else if (results.size() == 1) return (ProductItem) results.get(0);
//		        throw new NonUniqueResultException();

		return null;


	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ProductItem> getProductsListByCategories(Set categoryIds) {


		//List regionList = new ArrayList();
		//regionList.add("*");
		//regionList.add(locale.getCountry());


		//TODO Test performance 
		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */


		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from ProductItem as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");

		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");


		qs.append("left join fetch pap.descriptions papd ");


		//images
		qs.append("left join fetch p.images images ");

		//options (do not need attributes for listings)
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");

		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch p.type type ");
		qs.append("left join fetch p.taxClass tx ");

		//RENTAL
		qs.append("left join fetch p.owner owner ");

		//qs.append("where pa.region in (:lid) ");
		qs.append("where categs.id in (:cid)");


//    	String hql = qs.toString();
//		Query q = this.em.createQuery(hql);
//
//    	q.setParameter("cid", categoryIds);


//    	@SuppressWarnings("unchecked")
//		List<ProductItem> products =  q.getResultList();


//    	return products;

		return null;

	}

	@Override
	public List<ProductItem> getProductsListByCategories(Set<Long> categoryIds, LocaleItem language) {


		//List regionList = new ArrayList();
		//regionList.add("*");
		//regionList.add(locale.getCountry());


		//TODO Test performance 
		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */


		StringBuilder qs = new StringBuilder();
		qs.append("select distinct p from ProductItem as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");

		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");


		qs.append("left join fetch pap.descriptions papd ");


		//images
		qs.append("left join fetch p.images images ");

		//options (do not need attributes for listings)
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");

		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");
		qs.append("left join fetch p.taxClass tx ");

		//RENTAL
		qs.append("left join fetch p.owner owner ");

		//qs.append("where pa.region in (:lid) ");
		qs.append("where categs.id in (:cid) ");
		//qs.append("and pd.language.id=:lang and papd.language.id=:lang and manufd.language.id=:lang ");
		qs.append("and pd.language.id=:lang and papd.language.id=:lang ");
		qs.append("and p.available=true and p.dateAvailable<=:dt ");


		String hql = qs.toString();
//		Query q = this.em.createQuery(hql);
//
//    	q.setParameter("cid", categoryIds);
//    	q.setParameter("lang", language.getId());
//    	q.setParameter("dt", new Date());


//    	@SuppressWarnings("unchecked")
//		List<ProductItem> products =  q.getResultList();


//    	return products;

		return null;

	}

	/**
	 * This query is used for category listings. All collections are not fully loaded, only the required objects
	 * so the listing page can display everything related to all products
	 */
	@SuppressWarnings({"rawtypes", "unchecked", "unused"})
	private ProductList getProductsListForLocale(MerchantStoreItem store, Set categoryIds, LocaleItem language, Locale locale, int first, int max) {


		List regionList = new ArrayList();
		regionList.add(Constants.ALL_REGIONS);
		if (locale != null) {
			regionList.add(locale.getCountry());
		}

		ProductList productList = new ProductList();


//				Query countQ = this.em.createQuery(
//							"select count(p) from ProductItem as p INNER JOIN p.availabilities pa INNER JOIN p.categories categs where p.merchantStore.id=:mId and categs.id in (:cid) and pa.region in (:lid) and p.available=1 and p.dateAvailable<=:dt");
//
//				countQ.setParameter("cid", categoryIds);
//				countQ.setParameter("lid", regionList);
//				countQ.setParameter("dt", new Date());
//				countQ.setParameter("mId", store.getId());
//
//				Number count = (Number) countQ.getSingleResult ();


//				productList.setTotalCount(count.intValue());

//				if(count.intValue()==0)
//		        	return productList;


		StringBuilder qs = new StringBuilder();
		qs.append("select p from ProductItem as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");

		qs.append("join fetch p.descriptions pd ");
		qs.append("join fetch p.categories categs ");


		//not necessary
		//qs.append("join fetch pap.descriptions papd ");


		//images
		qs.append("left join fetch p.images images ");

		//options (do not need attributes for listings)
		//qs.append("left join fetch p.attributes pattr ");
		//qs.append("left join fetch pattr.productOption po ");
		//qs.append("left join fetch po.descriptions pod ");
		//qs.append("left join fetch pattr.productOptionValue pov ");
		//qs.append("left join fetch pov.descriptions povd ");

		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");
		qs.append("left join fetch p.taxClass tx ");

		//RENTAL
		qs.append("left join fetch p.owner owner ");

		//qs.append("where pa.region in (:lid) ");
		qs.append("where p.merchantStore.id=mId and categs.id in (:cid) and pa.region in (:lid) ");
		//qs.append("and p.available=true and p.dateAvailable<=:dt and pd.language.id=:lang and manufd.language.id=:lang");
		qs.append("and p.available=true and p.dateAvailable<=:dt and pd.language.id=:lang");
		qs.append(" order by p.sortOrder asc");


		String hql = qs.toString();
//				Query q = this.em.createQuery(hql);
//
//		    	q.setParameter("cid", categoryIds);
//		    	q.setParameter("lid", regionList);
//		    	q.setParameter("dt", new Date());
//		    	q.setParameter("lang", language.getId());
//		    	q.setParameter("mId", store.getId());


//		    	q.setFirstResult(first);
//		    	if(max>0) {
//		    			int maxCount = first + max;
//
//		    			if(maxCount < count.intValue()) {
//		    				q.setMaxResults(maxCount);
//		    			} else {
//		    				q.setMaxResults(count.intValue());
//		    			}
//		    	}

//		    	List<ProductItem> products =  q.getResultList();
//		    	productList.setProducts(products);

		return productList;


	}

	/**
	 * This query is used for filtering products based on criterias
	 *
	 * @param store
	 * @return
	 */
	@Override
	public ProductList listByStore(MerchantStoreItem store, LocaleItem language, ProductCriteria criteria) {

		ProductList productList = new ProductList();


		StringBuilder qs = new StringBuilder();
		qs.append("SELECT distinct * FROM " + ProductItem.ITEM_TYPE + " AS p ");
		if (!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			qs.append(" JOIN " + CategoryProductRelation.ITEM_TYPE + " AS cpr ON (cpr.target = p.uuid)");
			qs.append(" JOIN " + CategoryItem.ITEM_TYPE + " AS c ON (cpr.source = c.uuid)");
		}

		//attributes
		if (!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			qs.append(" JOIN " + ProductProductAttributeRelation.ITEM_TYPE + " AS pattrr ON (pattrr.source = p.uuid)");
			qs.append(" JOIN " + ProductAttributeItem.ITEM_TYPE + " AS pattr ON (pattrr.target = pattr.uuid)");
		}

		qs.append(" WHERE p.merchantStore=:mId");

		if (!CollectionUtils.isEmpty(criteria.getProductIds())) {
			qs.append(" AND p.uuid IN (:pId)");
		}

		if (!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			qs.append(" AND c.uuid IN (:cid)");
		}

		if (criteria.getManufacturerId() != null) {
			qs.append(" AND p.manufacturer = :manufid");
		}

		if (criteria.getAvailable() != null) {
			if (criteria.getAvailable()) {
				qs.append(" AND p.available = :available AND (p.dateAvailable IS NULL OR p.dateAvailable <= :dt)");
			} else {
				qs.append(" AND p.available = :available AND (p.dateAvailable IS NULL OR p.dateAvailable > :dt)");
			}
		}

		if (!StringUtils.isBlank(criteria.getCode())) {
			qs.append(" AND lower(p.sku) LIKE :sku");
		}

		//RENTAL
		if (!StringUtils.isBlank(criteria.getStatus())) {
			qs.append(" AND p.rentalStatus = :status");
		}

		if (criteria.getOwnerId() != null) {
			qs.append(" AND p.owner = :ownerid");
		}

		if (!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			for (AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				qs.append(" AND po.code =:").append(attributeCriteria.getAttributeCode());
			}

		}
		qs.append(" order by p.sortOrder asc");


		Map<String, Object> params = Maps.newHashMap();

		params.put("mId", store.getUuid());

		if (!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
			params.put("cid", criteria.getCategoryIds());
		}

		if (!CollectionUtils.isEmpty(criteria.getProductIds())) {
			params.put("pId", criteria.getProductIds());
		}

		if (criteria.getAvailable() != null) {
			params.put("dt", new Date());
			params.put("available", criteria.getAvailable());
		}

		if (criteria.getManufacturerId() != null) {
			params.put("manufid", criteria.getManufacturerId());
		}

		if (!StringUtils.isBlank(criteria.getCode())) {
			params.put("sku", new StringBuilder().append("%").append(criteria.getCode().toLowerCase()).append("%").toString());
		}

		if (!CollectionUtils.isEmpty(criteria.getAttributeCriteria())) {
			for (AttributeCriteria attributeCriteria : criteria.getAttributeCriteria()) {
				params.put(attributeCriteria.getAttributeCode(), attributeCriteria.getAttributeCode());
			}
		}

		//RENTAL
		if (!StringUtils.isBlank(criteria.getStatus())) {
			params.put("status", RentalStatusEnum.fromString(criteria.getStatus()));
		}

		if (criteria.getOwnerId() != null) {
			params.put("ownerid", criteria.getOwnerId());
		}

		var searchResult = getSearchService().<ProductItem>search(qs.toString(), params);

//    	if(criteria.getMaxCount()>0) {
//
//
//	    	q.setFirstResult(criteria.getStartIndex());
//	    	if(criteria.getMaxCount()<count.intValue()) {
//	    		q.setMaxResults(criteria.getMaxCount());
//	    	}
//	    	else {
//	    		q.setMaxResults(count.intValue());
//	    	}
//    	}

//    	@SuppressWarnings("unchecked")
		List<ProductItem> products = searchResult.getResult();
		productList.setProducts(products);

		return productList;


	}

	@Override
	public List<ProductItem> listByStore(MerchantStoreItem store) {


		/**
		 * Testing in debug mode takes a long time with this query
		 * running in normal mode is fine
		 */


		StringBuilder qs = new StringBuilder();
		qs.append("select p from ProductItem as p ");
		qs.append("join fetch p.merchantStore merch ");
		qs.append("join fetch p.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");

		qs.append("join fetch p.descriptions pd ");
		qs.append("left join fetch p.categories categs ");


		qs.append("left join fetch pap.descriptions papd ");


		//images
		qs.append("left join fetch p.images images ");

		//options (do not need attributes for listings)
		qs.append("left join fetch p.attributes pattr ");
		qs.append("left join fetch pattr.productOption po ");
		qs.append("left join fetch po.descriptions pod ");
		qs.append("left join fetch pattr.productOptionValue pov ");
		qs.append("left join fetch pov.descriptions povd ");

		//other lefts
		qs.append("left join fetch p.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch p.type type ");
		qs.append("left join fetch p.taxClass tx ");

		//RENTAL
		qs.append("left join fetch p.owner owner ");

		//qs.append("where pa.region in (:lid) ");
		qs.append("where merch.id=:mid");


		String hql = qs.toString();
//		Query q = this.em.createQuery(hql);
//
//    	q.setParameter("mid", store.getId());


//    	@SuppressWarnings("unchecked")
//		List<ProductItem> products =  q.getResultList();


//    	return products;
		return null;

	}


}

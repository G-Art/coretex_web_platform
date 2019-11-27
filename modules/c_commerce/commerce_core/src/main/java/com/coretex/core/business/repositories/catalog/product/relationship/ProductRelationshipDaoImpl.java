package com.coretex.core.business.repositories.catalog.product.relationship;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.core.LocaleItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductRelationshipDaoImpl extends DefaultGenericDao<ProductRelationshipItem> implements ProductRelationshipDao {

	public ProductRelationshipDaoImpl() {
		super(ProductRelationshipItem.ITEM_TYPE);
	}

	@Override
	public List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type, ProductItem product, LocaleItem language) {

		StringBuilder qs = new StringBuilder();
		qs.append("select distinct pr from ProductRelationshipItem as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("join fetch pr.relatedProduct rp ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where pr.code=:code ");
		qs.append("and pr.store.id=:storeId ");
		qs.append("and p.id=:id ");
		qs.append("and rpd.language.id=:langId");


		String hql = qs.toString();
//		Query q = em.createQuery(hql);
//
//    	q.setParameter("code", type);
//    	q.setParameter("id", product.getUuid());
//    	q.setParameter("storeId", store.getUuid());
//    	qs.append("and pr.store.id=:storeId ");
//    	q.setParameter("langId", language.getUuid());
//
//
//
//    	@SuppressWarnings("unchecked")
//		List<ProductRelationshipItem> relations =  q.getResultList();


//    	return relations;
		return null;

	}

	@Override
	public List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type, LocaleItem language) {

		StringBuilder qs = new StringBuilder();
		qs.append("SELECT distinct * FROM "+ ProductRelationshipItem.ITEM_TYPE +" AS pr ");
		qs.append(" JOIN "+ ProductItem.ITEM_TYPE +" AS rp ON rp.uuid = pr." + ProductRelationshipItem.RELATED_PRODUCT);
		qs.append(" JOIN "+ MerchantStoreItem.ITEM_TYPE +" AS s ON s.uuid = pr."+ProductRelationshipItem.STORE);

		qs.append(" WHERE pr."+ProductRelationshipItem.CODE+"=:code ");
		qs.append("AND rp."+ProductItem.AVAILABLE+"=:available ");
		qs.append("AND s.uuid=:"+ProductRelationshipItem.STORE);


		String query = qs.toString();
		return getSearchService()
				.<ProductRelationshipItem>search(query, Map.of(ProductRelationshipItem.CODE, type, ProductItem.AVAILABLE, true, ProductRelationshipItem.STORE, store))
				.getResult();

	}

	@Override
	public List<ProductRelationshipItem> getByGroup(MerchantStoreItem store, String group) {

		StringBuilder qs = new StringBuilder();
		qs.append("select distinct pr from ProductRelationshipItem as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("left join fetch pr.relatedProduct rp ");

		qs.append("left join fetch rp.attributes pattr ");
		qs.append("left join fetch rp.categories rpc ");
		qs.append("left join fetch rpc.descriptions rpcd ");
		qs.append("left join fetch rp.descriptions rpd ");
		qs.append("left join fetch rp.owner rpo ");
		qs.append("left join fetch rp.images pd ");
		qs.append("left join fetch rp.merchantStore rpm ");
		qs.append("left join fetch rpm.currency rpmc ");
		qs.append("left join fetch rp.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");
		qs.append("left join fetch rp.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch rp.type type ");

		qs.append("where pr.code=:code ");
		qs.append("and pr.store.id=:storeId ");


//    	String hql = qs.toString();
//		Query q = em.createQuery(hql);
//
//    	q.setParameter("code", group);
//    	q.setParameter("storeId", store.getId());


//    	@SuppressWarnings("unchecked")
//		List<ProductRelationshipItem> relations =  q.getResultList();


//    	return relations;
		return null;

	}

	@Override
	public List<ProductRelationshipItem> getGroups(MerchantStoreItem store) {

		StringBuilder qs = new StringBuilder();
		qs.append("select distinct pr from ProductRelationshipItem as pr ");
		qs.append("where pr.store.id=:store ");

		qs.append("and pr.product=null");


//    	String hql = qs.toString();
//		Query q = em.createQuery(hql);
//
//    	q.setParameter("store", store.getId());


//    	@SuppressWarnings("unchecked")
//		List<ProductRelationshipItem> relations =  q.getResultList();

//    	Map<String, ProductRelationshipItem> relationMap = new HashMap<String, ProductRelationshipItem>();
//    	for(ProductRelationshipItem relationship : relations) {
//    		if(!relationMap.containsKey(relationship.getCode())) {
//    			relationMap.put(relationship.getCode(), relationship);
//    		}
//    	}
//
//    	List<ProductRelationshipItem> returnList = new ArrayList<ProductRelationshipItem>(relationMap.values());
//
//
//    	return returnList;
		return null;

	}


	@Override
	public List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type) {

		StringBuilder qs = new StringBuilder();
		qs.append("select distinct pr from ProductRelationshipItem as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("join fetch pr.relatedProduct rp ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where pr.code=:code ");
		qs.append("and pr.store.id=:storeId ");


//    	String hql = qs.toString();
//		Query q = em.createQuery(hql);
//
//    	q.setParameter("code", type);
//    	q.setParameter("storeId", store.getId());
//
//
//    	@SuppressWarnings("unchecked")
//		List<ProductRelationshipItem> relations =  q.getResultList();


//    	return relations;

		return null;

	}

	@Override
	public List<ProductRelationshipItem> listByProducts(ProductItem product) {

		StringBuilder qs = new StringBuilder();
		qs.append("select pr from ProductRelationshipItem as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("left join fetch pr.relatedProduct rp ");
		qs.append("left join fetch rp.attributes pattr ");
		qs.append("left join fetch rp.categories rpc ");
		qs.append("left join fetch p.descriptions pd ");
		qs.append("left join fetch rp.descriptions rpd ");

		qs.append("where p.id=:id");
		qs.append(" or rp.id=:id");


//    	String hql = qs.toString();
//		Query q = em.createQuery(hql);
//
//    	q.setParameter("id", product.getId());
//
//
//    	@SuppressWarnings("unchecked")
//		List<ProductRelationshipItem> relations =  q.getResultList();


//    	return relations;
		return null;

	}

	@Override
	public List<ProductRelationshipItem> getByType(MerchantStoreItem store, String type, ProductItem product) {

		StringBuilder qs = new StringBuilder();

		qs.append("select distinct pr from ProductRelationshipItem as pr ");
		qs.append("left join fetch pr.product p ");
		qs.append("left join fetch pr.relatedProduct rp ");

		qs.append("left join fetch rp.attributes pattr ");
		qs.append("left join fetch rp.categories rpc ");
		qs.append("left join fetch rpc.descriptions rpcd ");
		qs.append("left join fetch rp.descriptions rpd ");
		qs.append("left join fetch rp.owner rpo ");
		qs.append("left join fetch rp.images pd ");
		qs.append("left join fetch rp.merchantStore rpm ");
		qs.append("left join fetch rpm.currency rpmc ");
		qs.append("left join fetch rp.availabilities pa ");
		qs.append("left join fetch pa.prices pap ");
		qs.append("left join fetch pap.descriptions papd ");

		qs.append("left join fetch rp.manufacturer manuf ");
		qs.append("left join fetch manuf.descriptions manufd ");
		qs.append("left join fetch rp.type type ");

		qs.append("where pr.code=:code ");
		qs.append("and rp.available=:available ");
		qs.append("and p.id=:pId");


//    	String hql = qs.toString();
//		Query q = em.createQuery(hql);
//
//    	q.setParameter("code", type);
//    	q.setParameter("available", true);
//    	q.setParameter("pId", product.getId());
//
//
//		@SuppressWarnings("unchecked")
//		List<ProductRelationshipItem> relations =  q.getResultList();


//    	return relations;

		return null;

	}

}

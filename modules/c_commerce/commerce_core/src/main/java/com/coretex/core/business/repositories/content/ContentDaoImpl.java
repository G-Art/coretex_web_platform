package com.coretex.core.business.repositories.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class ContentDaoImpl extends DefaultGenericDao<ContentItem> implements ContentDao {


	public ContentDaoImpl() {
		super(ContentItem.ITEM_TYPE);
	}

	@Override
	public List<ContentItem> listNameByType(List<ContentTypeEnum> contentType, MerchantStoreItem store, LanguageItem language) {


		StringBuilder qs = new StringBuilder();

		qs.append("select c from ContentItem c ");
		qs.append("left join fetch c.descriptions cd join fetch c.merchantStore cm ");
		qs.append("where c.contentType in (:ct) ");
		qs.append("and cm.id =:cm ");
		qs.append("and cd.language.id =:cl ");
		qs.append("order by c.sortOrder");

		String hql = qs.toString();
//			Query q = this.em.createQuery(hql);

//	    	q.setParameter("ct", contentType);
//	    	q.setParameter("cm", store.getId());
//	    	q.setParameter("cl", language.getId());


		@SuppressWarnings("unchecked")
//			List<ContentItem> contents = q.getResultList();

				List<ContentItem> descriptions = new ArrayList<>();
//			for(ContentItem c : contents) {
//					descriptions.add(c.getName());
//
//			}

		return descriptions;

	}

	@Override
	public ContentItem getBySeUrl(MerchantStoreItem store, String seUrl) {

		StringBuilder qs = new StringBuilder();

		qs.append("select c from ContentItem c ");
		qs.append("left join fetch c.descriptions cd join fetch c.merchantStore cm ");
		qs.append("where cm.id =:cm ");
		qs.append("and cd.seUrl =:se ");


		String hql = qs.toString();
//			Query q = this.em.createQuery(hql);
//
//	    	q.setParameter("cm", store.getId());
//	    	q.setParameter("se", seUrl);


//	    	ContentItem content = (ContentItem)q.getSingleResult();


//			if(content!=null) {
//					return content.getDescription();
//			}

//			@SuppressWarnings("unchecked")
//			List<ContentItem> results = q.getResultList();
//	        if (results.isEmpty()) {
//	        	return null;
//	        } else if (results.size() >= 1) {
//	        		content = results.get(0);
//	        }

//			if(content!=null) {
//				return content.getDescription();
//			}


		return null;

	}


	@Override
	public List<ContentItem> findByType(ContentTypeEnum contentType, UUID storeId, UUID languageId) {
		return null;
	}

	@Override
	public List<ContentItem> findByType(ContentTypeEnum contentType, UUID storeId) {
		return null;
	}

	@Override
	public List<ContentItem> findByTypes(List<ContentTypeEnum> contentTypes, UUID storeId, UUID languageId) {
		return null;
	}

	@Override
	public List<ContentItem> findByTypes(List<ContentTypeEnum> contentTypes, UUID storeId) {
		return null;
	}

	@Override
	public ContentItem findByCode(String code, UUID storeId) {
		return null;
	}

	@Override
	public List<ContentItem> findByCodeLike(ContentTypeEnum contentType, String code, UUID storeId, UUID languageId) {
		return null;
	}

	@Override
	public ContentItem findByCode(String code, UUID storeId, UUID languageId) {
		return findSingle(Map.of(ContentItem.CODE, code, ContentItem.MERCHANT_STORE, storeId), true);
	}

	@Override
	public ContentItem findByIdAndLanguage(UUID contentId, UUID languageId) {
		return null;
	}

	@Override
	public ContentItem findOne(UUID contentId) {
		return find(contentId);
	}
}
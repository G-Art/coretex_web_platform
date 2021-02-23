package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.CategoryDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.relations.cx_core.CategoryCategoryRelation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class DefaultCategoryDao extends DefaultGenericDao<CategoryItem> implements CategoryDao {

	public DefaultCategoryDao() {
		super(CategoryItem.ITEM_TYPE);
	}

	@Override
	public Flux<CategoryItem> findByParent(UUID parentId) {

		String qs = "SELECT * FROM " + CategoryItem.ITEM_TYPE + " AS category ";

		if(Objects.isNull(parentId)){
			qs = qs + "LEFT JOIN " + CategoryCategoryRelation.ITEM_TYPE + " AS categories ON (categories.target = category.uuid) " +
					"WHERE categories.target IS NULL";
		}else{
			qs = qs + "LEFT JOIN " + CategoryCategoryRelation.ITEM_TYPE + " AS categories ON (categories.target = category.uuid) " +
					"WHERE categories.source = :cid";
		}
		if(Objects.isNull(parentId)){
			return getSearchService().<CategoryItem>search(qs).getResultStream();
		}
		return getSearchService().<CategoryItem>search(qs, Map.of("cid", parentId)).getResultStream();
	}
}

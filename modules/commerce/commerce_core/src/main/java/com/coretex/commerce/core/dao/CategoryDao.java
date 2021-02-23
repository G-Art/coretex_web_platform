package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.CategoryItem;
import reactor.core.publisher.Flux;

import java.util.UUID;


public interface CategoryDao extends Dao<CategoryItem> {

	Flux<CategoryItem> findByParent(UUID uuid);
}

package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.CategoryItem;

import java.util.UUID;
import java.util.stream.Stream;


public interface CategoryDao extends Dao<CategoryItem> {

	Stream<CategoryItem> findByParent(UUID uuid);
}

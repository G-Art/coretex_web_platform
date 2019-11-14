package com.coretex.core.activeorm.dao;

import com.coretex.meta.AbstractGenericItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Dao<I extends AbstractGenericItem> {

	void save(I item);

	void delete(I item);

	Long count();

	List<I> find();

	I find(UUID uuid);

	I findSingle(Map<String, ?> params, boolean throwAmbiguousException);

	List<I> find(String query);

	List<I> find(String query, Map<String, Object> params);

	List<I> find(Map<String, ?> var1);

	List<I> find(SortParameters var1);

	List<I> find(Map<String, ?> var1, SortParameters var2);

	List<I> find(Map<String, ?> var1, SortParameters var2, int var3);

	List<I> find(Map<String, ?> params, SortParameters sortParameters, int count, int start);
}

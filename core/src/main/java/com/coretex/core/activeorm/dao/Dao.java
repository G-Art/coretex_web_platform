package com.coretex.core.activeorm.dao;

import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.meta.AbstractGenericItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public interface Dao<I extends AbstractGenericItem> {

	void save(I item);

	void delete(I item);

	Long count();

	List<I> find();
	Stream<I> findReactive();

	I find(UUID uuid);

	I findSingle(Map<String, ?> params, boolean throwAmbiguousException);

	List<I> find(String query);
	Stream<I> findReactive(String query);

	List<I> find(String query, Map<String, Object> params);
	Stream<I> findReactive(String query, Map<String, Object> params);

	List<I> find(Map<String, ?> var1);
	Stream<I> findReactive(Map<String, ?> var1);

	List<I> find(SortParameters var1);
	Stream<I> findReactive(SortParameters var1);

	List<I> find(Map<String, ?> var1, SortParameters var2);
	Stream<I> findReactive(Map<String, ?> var1, SortParameters var2);

	List<I> find(Map<String, ?> var1, SortParameters var2, long count);
	Stream<I> findReactive(Map<String, ?> var1, SortParameters var2, long count);

	List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, long page);
	Stream<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, long page);

	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count);

	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, long page);

	PageableSearchResult<I> findPageable(Map<String, ?> params, long count, long page);

	PageableSearchResult<I> findPageable(Map<String, ?> params, long count);

	PageableSearchResult<I> findPageable(Map<String, ?> params);

	PageableSearchResult<I> findPageable(long count, long page);

	PageableSearchResult<I> findPageable(long count);

	PageableSearchResult<I> findPageable();

}

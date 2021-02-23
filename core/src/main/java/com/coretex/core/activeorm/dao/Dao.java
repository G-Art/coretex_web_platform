package com.coretex.core.activeorm.dao;

import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.meta.AbstractGenericItem;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Dao<I extends AbstractGenericItem> {

	void save(I item);

	void delete(I item);

	Long count();
	Long count(boolean strict);

	List<I> find();
	List<I> find(boolean strict);

	Flux<I> findReactive();
	Flux<I> findReactive(boolean strict);

	I find(UUID uuid);
	I find(UUID uuid, boolean strict);

	I findSingle(Map<String, ?> params, boolean throwAmbiguousException);
	I findSingle(Map<String, ?> params, boolean throwAmbiguousException, boolean strict);

	Optional<I> findOne(Map<String, ?> params, boolean throwAmbiguousException);
	Optional<I> findOne(Map<String, ?> params, boolean throwAmbiguousException, boolean strict);

	List<I> find(String query);
	Flux<I> findReactive(String query);

	List<I> find(String query, Map<String, Object> params);
	Flux<I> findReactive(String query, Map<String, Object> params);

	List<I> find(Map<String, ?> params);
	List<I> find(Map<String, ?> params, boolean strict);

	Flux<I> findReactive(Map<String, ?> params);
	Flux<I> findReactive(Map<String, ?> params, boolean strict);

	List<I> find(SortParameters sortParameters);
	List<I> find(SortParameters sortParameters, boolean strict);

	Flux<I> findReactive(SortParameters sortParameters);
	Flux<I> findReactive(SortParameters sortParameters, boolean strict);

	List<I> find(Map<String, ?> params, SortParameters sortParameters);
	List<I> find(Map<String, ?> params, SortParameters sortParameters, boolean strict);

	Flux<I> findReactive(Map<String, ?> params, SortParameters sortParameters);
	Flux<I> findReactive(Map<String, ?> params, SortParameters sortParameters, boolean strict);

	List<I> find(Map<String, ?> params, SortParameters sortParameters, long count);
	List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict);

	Flux<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count);
	Flux<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict);

	List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, long page);
	List<I> find(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict);

	Flux<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, long page);
	Flux<I> findReactive(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count);
	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, long page);
	PageableSearchResult<I> findPageable(Map<String, ?> params, SortParameters sortParameters, long count, long page, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params, long count);
	PageableSearchResult<I> findPageable(Map<String, ?> params, long count, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params, long count, long page);
	PageableSearchResult<I> findPageable(Map<String, ?> params, long count, long page, boolean strict);

	PageableSearchResult<I> findPageable(Map<String, ?> params);
	PageableSearchResult<I> findPageable(Map<String, ?> params, boolean strict);

	PageableSearchResult<I> findPageable(long count, long page);
	PageableSearchResult<I> findPageable(long count, long page, boolean strict);

	PageableSearchResult<I> findPageable(long count);
	PageableSearchResult<I> findPageable(long count, boolean strict);

	PageableSearchResult<I> findPageable();
	PageableSearchResult<I> findPageable(boolean strict);
}

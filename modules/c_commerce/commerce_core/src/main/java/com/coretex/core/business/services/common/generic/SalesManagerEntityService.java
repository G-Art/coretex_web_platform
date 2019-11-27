package com.coretex.core.business.services.common.generic;

import com.coretex.core.activeorm.services.PageableSearchResult;

import java.util.List;
import java.util.UUID;

public interface SalesManagerEntityService<E> extends TransactionalAspectAwareService {

	void save(E entity);

	@Deprecated
	void update(E entity);

	void create(E entity);

	void delete(E entity);

	E getByUUID(UUID id);

	List<E> list();

	Long count();

	PageableSearchResult<E> pageableList();

	PageableSearchResult<E> pageableList(long count);
	PageableSearchResult<E> pageableList(long count, long page);

}

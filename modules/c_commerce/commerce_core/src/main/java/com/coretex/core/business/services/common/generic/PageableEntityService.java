package com.coretex.core.business.services.common.generic;

import com.coretex.core.activeorm.services.PageableSearchResult;

import java.util.List;
import java.util.UUID;

public interface PageableEntityService<E> {

	PageableSearchResult<E> pageableList();

	PageableSearchResult<E> pageableList(long count);
	PageableSearchResult<E> pageableList(long count, long page);

}

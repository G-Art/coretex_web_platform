package com.coretex.commerce.core.services;

import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.core.GenericItem;

public interface PageableService<E extends GenericItem> {

	PageableSearchResult<E> pageableList();

	PageableSearchResult<E> pageableList(long count);
	PageableSearchResult<E> pageableList(long count, long page);

}

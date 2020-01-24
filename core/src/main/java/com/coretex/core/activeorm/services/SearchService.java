package com.coretex.core.activeorm.services;

import com.coretex.core.activeorm.query.specs.select.PageableSelectOperationSpec;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;

import java.util.Map;

public interface SearchService{

	<T> SearchResult<T> search(String query);

	<T> SearchResult<T> search(String query, Map<String, Object> parameters);

	<T> SearchResult<T> search(SelectOperationSpec<T> spec);

	<T> PageableSearchResult<T> searchPageable(String query);

	<T> PageableSearchResult<T> searchPageable(String query, long count);

	<T> PageableSearchResult<T> searchPageable(String query, long count, long page);

	<T> PageableSearchResult<T> searchPageable(String query, Map<String, Object> parameters, long count, long page);

	<T> PageableSearchResult<T> searchPageable(String query, Map<String, Object> parameters);

	<T> PageableSearchResult<T> searchPageable(PageableSelectOperationSpec<T> spec);
}

package com.coretex.core.activeorm.services;

import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;

import java.util.Map;

public interface SearchService {

	<T> SearchResult<T> search(String query);

	<T> SearchResult<T> search(String query, Map<String, Object> parameters);

	<T> SearchResult<T> search(SelectOperationSpec<T> spec);
}

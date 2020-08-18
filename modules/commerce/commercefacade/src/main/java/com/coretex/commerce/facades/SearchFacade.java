package com.coretex.commerce.facades;

import com.coretex.commerce.data.SearchPageResult;

import java.util.List;
import java.util.Map;

public interface SearchFacade {
	SearchPageResult getSearchPage(String query, int page, int size, Map<String, List<String>> filter, Map<String, List<String>> sort);
}

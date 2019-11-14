package com.coretex.search.services.worker;

import com.coretex.search.services.SearchResponse;
import com.coretex.search.utils.SearchClient;

public interface KeywordSearchWorker {

	SearchResponse execute(SearchClient client, String collection, String json, int size,
						   ExecutionContext context) throws Exception;

}

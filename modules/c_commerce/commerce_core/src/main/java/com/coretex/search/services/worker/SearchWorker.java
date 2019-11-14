package com.coretex.search.services.worker;

import com.coretex.search.services.SearchRequest;
import com.coretex.search.services.SearchResponse;
import com.coretex.search.utils.SearchClient;

public interface SearchWorker {

	SearchResponse execute(SearchClient client, SearchRequest request,
						   ExecutionContext context) throws Exception;

}

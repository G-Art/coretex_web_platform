package com.coretex.search.services.worker;

import javax.inject.Inject;

import com.coretex.search.services.SearchRequest;
import com.coretex.search.services.SearchResponse;
import com.coretex.search.services.impl.SearchDelegate;
import com.coretex.search.utils.SearchClient;


public class SearchWorkerImpl implements SearchWorker {

	@Inject
	private SearchDelegate searchDelegate;

	public SearchResponse execute(SearchClient client, SearchRequest request,
								  ExecutionContext context) throws Exception {

		SearchResponse response = searchDelegate.search(request);

		response.setInputSearchJson(request.getJson());
		if (context == null) {
			context = new ExecutionContext();
		}
		context.setObject("response", response);
		return response;

	}

}

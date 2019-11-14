package com.coretex.search.services.worker;

import com.coretex.search.utils.SearchClient;

public interface IndexWorker {

	void init(SearchClient client);

	void execute(SearchClient client, String json, String collection, String object, String id,
				 ExecutionContext context) throws Exception;

}

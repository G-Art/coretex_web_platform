package com.coretex.search.services.workflow;

import javax.inject.Inject;

import com.coretex.search.services.GetResponse;
import org.springframework.stereotype.Component;
import com.coretex.search.services.impl.SearchDelegate;


@Component
public class GetWorkflow extends Workflow {

	@Inject
	private SearchDelegate searchDelegate;

	public GetResponse getObject(String collection, String object,
								 String id) throws Exception {

		return searchDelegate.getObject(collection, object, id);

	}

}

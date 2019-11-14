package com.coretex.search.services.workflow;

import com.coretex.search.utils.SearchClient;

public class Workflow {

	private SearchClient searchClient;


	public SearchClient getSearchClient() {
		return searchClient;
	}


	public void setSearchClient(SearchClient searchClient) {
		this.searchClient = searchClient;
	}


	public Workflow() {
		super();
	}


}

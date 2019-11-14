package com.coretex.search.services.workflow;

import java.util.List;

import org.springframework.stereotype.Component;
import com.coretex.search.services.SearchRequest;
import com.coretex.search.services.SearchResponse;
import com.coretex.search.services.worker.KeywordSearchWorker;
import com.coretex.search.services.worker.SearchWorker;


@Component
@SuppressWarnings("rawtypes")
public class SearchWorkflow extends Workflow {


	private List searchFlow;
	private List searchKeywordWorkflow;


	public List getSearchKeywordWorkflow() {
		return searchKeywordWorkflow;
	}

	public void setSearchKeywordWorkflow(List searchKeywordWorkflow) {
		this.searchKeywordWorkflow = searchKeywordWorkflow;
	}


	public SearchResponse searchAutocomplete(String collection, String json, int size)
			throws Exception {


		SearchResponse response = null;


		if (searchKeywordWorkflow != null) {
			for (Object o : searchKeywordWorkflow) {

				// String className = (String)o;
				// SearchWorker search = (SearchWorker)Class.forName(className).newInstance();
				// search.execute(request.getJson(), request.getCollection());
				KeywordSearchWorker sw = (KeywordSearchWorker) o;
				response = sw.execute(super.getSearchClient(), collection, json, size, null);
			}
		}

		return response;

	}

	public SearchResponse search(SearchRequest request) throws Exception {


		SearchResponse response = null;


		if (searchFlow != null) {
			for (Object o : searchFlow) {
				SearchWorker sw = (SearchWorker) o;
				response = sw.execute(super.getSearchClient(), request, null);
			}
		}

		return response;

	}

	public List getSearchFlow() {
		return searchFlow;
	}

	public void setSearchFlow(List searchFlow) {
		this.searchFlow = searchFlow;
	}

}

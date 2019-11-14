package com.coretex.search.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.coretex.search.services.worker.KeywordIndexerImpl;
import com.coretex.search.services.worker.ObjectIndexerImpl;
import com.coretex.search.services.workflow.DeleteObjectWorkflow;
import com.coretex.search.services.workflow.GetWorkflow;
import com.coretex.search.services.workflow.IndexWorkflow;
import com.coretex.search.services.workflow.SearchWorkflow;
import com.coretex.search.utils.SearchClient;


/**
 * This is the main class for indexing and searching services
 *
 * @author Carl Samson
 */

public class SearchingService {


	private static Logger log = LoggerFactory.getLogger(SearchingService.class);

	@Autowired
	private DeleteObjectWorkflow deleteWorkflow;

	@Autowired
	private IndexWorkflow indexWorkflow;

	@Autowired
	private GetWorkflow getWorkflow;

	@Autowired
	private SearchWorkflow searchWorkflow;

	@Autowired
	private ObjectIndexerImpl index;

	@Autowired
	private KeywordIndexerImpl keyword;

	@Autowired
	private SearchClient searchClient;

	public void initService() {
		log.debug("Initializing search service");

		try {
			index.init(searchClient);
			keyword.init(searchClient);
		} catch (Exception e) {
			log.error("Cannot initialize SearchingService correctly, will be initialized lazily", e);
		}

	}


	public void deleteObject(String collection, String object, String id) throws Exception {
		deleteWorkflow.deleteObject(collection, object, id);

	}


	public com.coretex.search.services.GetResponse getObject(String collection, String object,
															 String id) throws Exception {

		return getWorkflow.getObject(collection, object, id);
	}

	/**
	 * Index a document
	 *
	 * @param json       - input string
	 * @param collection (name of the collection) Might be product_en or product_fr or any name of the
	 *                   index container
	 * @param object     to index that corresponds to the name of the entity to be indexed as defined in
	 *                   the indice file (product.json). In this case it will be product
	 * @throws Exception indexing fails
	 */

	public void index(String json, String collection, String object) throws Exception {

		indexWorkflow.index(json, collection, object);
	}


	public SearchResponse searchAutoComplete(String collection, String json, int size)
			throws Exception {

		return searchWorkflow.searchAutocomplete(collection, json, size);
	}

	public SearchResponse search(SearchRequest request) throws Exception {

		return searchWorkflow.search(request);
	}
}

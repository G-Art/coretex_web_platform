package com.coretex.searchengine.solr.server;

import com.coretex.searchengine.solr.exceptions.SolrServerException;

import java.util.Map;
import java.util.Set;

public interface SolrServerCommandExecutor {
	void executeCommand(String var1, Map<String, String> var2) throws SolrServerException;

	Set<String> commands();
}

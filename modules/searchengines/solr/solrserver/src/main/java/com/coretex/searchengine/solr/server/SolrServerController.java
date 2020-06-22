package com.coretex.searchengine.solr.server;

import com.coretex.searchengine.solr.exceptions.SolrServerException;

public interface SolrServerController {
	void startServers() throws SolrServerException;

	void stopServers() throws SolrServerException;

	void creteSolrInstance() throws SolrServerException;
}

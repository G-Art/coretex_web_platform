package com.coretex.searchengine.solr.server;

import com.coretex.searchengine.solr.exceptions.SolrServerException;

import java.util.Map;

@FunctionalInterface
public interface SolrServerCommand {
	void execute(Map<String, String> params) throws SolrServerException;
}

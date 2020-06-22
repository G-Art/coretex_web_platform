package com.coretex.searchengine.solr.server;

import com.coretex.searchengine.solr.exceptions.SolrServerException;

import java.util.Collection;
import java.util.Map;

public interface SolrInstanceFactory {
	SolrInstance getInstanceForName(Map<String, String> var1, String var2) throws SolrServerException;

	Collection<SolrInstance> getInstances(Map<String, String> var1) throws SolrServerException;
}

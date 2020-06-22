package com.coretex.searchengine.solr.server;

import java.util.Map;

public interface SolrServerConfigurationProvider {
	Map<String, String> getConfiguration();
}

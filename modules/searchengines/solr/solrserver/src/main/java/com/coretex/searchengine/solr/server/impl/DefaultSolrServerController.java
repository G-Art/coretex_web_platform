package com.coretex.searchengine.solr.server.impl;

import com.coretex.searchengine.solr.exceptions.SolrServerException;
import com.coretex.searchengine.solr.server.SolrServerCommandExecutor;
import com.coretex.searchengine.solr.server.SolrServerConfigurationProvider;
import com.coretex.searchengine.solr.server.SolrServerController;

import java.util.Map;

public class DefaultSolrServerController implements SolrServerController {
	public static final String CREATE_SOLR_INSTANCE = "createSolrInstance";
	public static final String START_SERVERS_COMMAND = "startSolrServer";
	public static final String STOP_SERVERS_COMMAND = "stopSolrServer";
	private SolrServerConfigurationProvider solrServerConfigurationProvider;
	private SolrServerCommandExecutor solrServerCommandExecutor;

	public DefaultSolrServerController() {
	}

	public void startServers() throws SolrServerException {
		Map<String, String> configuration = this.solrServerConfigurationProvider.getConfiguration();
		this.solrServerCommandExecutor.executeCommand(START_SERVERS_COMMAND, configuration);
	}

	public void stopServers() throws SolrServerException {
		Map<String, String> configuration = this.solrServerConfigurationProvider.getConfiguration();
		this.solrServerCommandExecutor.executeCommand(STOP_SERVERS_COMMAND, configuration);
	}

	@Override
	public void creteSolrInstance() throws SolrServerException {
		Map<String, String> configuration = this.solrServerConfigurationProvider.getConfiguration();
		this.solrServerCommandExecutor.executeCommand(CREATE_SOLR_INSTANCE, configuration);
	}

	public SolrServerConfigurationProvider getSolrServerConfigurationProvider() {
		return this.solrServerConfigurationProvider;
	}

	public void setSolrServerConfigurationProvider(SolrServerConfigurationProvider solrServerConfigurationProvider) {
		this.solrServerConfigurationProvider = solrServerConfigurationProvider;
	}

	public SolrServerCommandExecutor getSolrServerCommandExecutor() {
		return this.solrServerCommandExecutor;
	}

	public void setSolrServerCommandExecutor(SolrServerCommandExecutor solrServerCommandExecutor) {
		this.solrServerCommandExecutor = solrServerCommandExecutor;
	}
}

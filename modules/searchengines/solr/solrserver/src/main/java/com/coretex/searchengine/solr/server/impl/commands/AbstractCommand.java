package com.coretex.searchengine.solr.server.impl.commands;

import com.coretex.core.shell.CommandExecutor;
import com.coretex.searchengine.solr.exceptions.SolrServerException;
import com.coretex.searchengine.solr.server.SolrInstance;
import com.coretex.searchengine.solr.server.SolrInstanceFactory;
import com.coretex.searchengine.solr.server.SolrServerCommand;

import java.util.Map;


public abstract class AbstractCommand implements SolrServerCommand {

	private SolrInstanceFactory solrInstanceFactory;
	private CommandExecutor commandExecutor;

	protected SolrInstance getSolrInstanceForName(Map<String, String> configuration) throws SolrServerException {
		String instanceName = configuration.get("instance.name");
		if (instanceName == null) {
			instanceName = "default";
		}

		return this.solrInstanceFactory.getInstanceForName(configuration, instanceName);
	}

	protected CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}

	public void setCommandExecutor(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}

	public void setSolrInstanceFactory(SolrInstanceFactory solrInstanceFactory) {
		this.solrInstanceFactory = solrInstanceFactory;
	}
}

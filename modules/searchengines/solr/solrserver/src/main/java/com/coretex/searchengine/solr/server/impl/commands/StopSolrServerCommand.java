package com.coretex.searchengine.solr.server.impl.commands;

import com.coretex.core.shell.CommandBuilder;
import com.coretex.searchengine.solr.exceptions.SolrServerException;
import com.coretex.searchengine.solr.server.SolrInstance;
import com.coretex.searchengine.solr.server.impl.DefaultSolrCommandBuilder;
import com.coretex.searchengine.solr.server.impl.SolrCommonParamsCommandBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class StopSolrServerCommand extends AbstractCommand {
	private static final Logger LOG = LoggerFactory.getLogger(StopSolrServerCommand.class.getName());
	protected static final String SOLR_STOP_COMMAND = "stop";

	public StopSolrServerCommand() {
	}

	public void execute(Map<String, String> configuration) {
		try {
			SolrInstance solrInstance = this.getSolrInstanceForName(configuration);
			this.executeCommand(configuration, solrInstance);
		} catch (SolrServerException ex) {
			LOG.info(ex.getMessage(), ex);
		}
	}

	protected void executeCommand(Map<String, String> configuration, SolrInstance solrInstance) {
		LOG.info("Stopping Solr server for instance {}", solrInstance);
		Collection<CommandBuilder> commandBuilders = Arrays.asList(new DefaultSolrCommandBuilder(configuration, solrInstance, SOLR_STOP_COMMAND), new SolrCommonParamsCommandBuilder(configuration, solrInstance));
		this.getCommandExecutor().execute(commandBuilders);
	}
}

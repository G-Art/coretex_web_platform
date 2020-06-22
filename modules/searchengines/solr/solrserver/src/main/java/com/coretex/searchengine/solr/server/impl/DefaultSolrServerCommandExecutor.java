package com.coretex.searchengine.solr.server.impl;

import com.coretex.searchengine.solr.exceptions.SolrServerException;
import com.coretex.searchengine.solr.server.SolrServerCommand;
import com.coretex.searchengine.solr.server.SolrServerCommandExecutor;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;

public class DefaultSolrServerCommandExecutor implements SolrServerCommandExecutor {

	private Map<String, SolrServerCommand> commands;

	public DefaultSolrServerCommandExecutor() {
	}

	public Map<String, SolrServerCommand> getCommands() {
		return this.commands;
	}

	public void setCommands(Map<String, SolrServerCommand> commands) {
		this.commands = commands;
	}

	public void executeCommand(String command, Map<String, String> configuration) throws SolrServerException {
		if (command != null && !command.isEmpty() && !command.contains(".")) {
			SolrServerCommand internalCommand = this.commands != null ? this.commands.get(command) : null;
			if (internalCommand != null) {
				internalCommand.execute(configuration);
			}
		} else {
			throw new SolrServerException(MessageFormat.format("Invalid command name for command ''{0}''", command));
		}
	}

	@Override
	public Set<String> commands() {
		return commands.keySet();
	}
}

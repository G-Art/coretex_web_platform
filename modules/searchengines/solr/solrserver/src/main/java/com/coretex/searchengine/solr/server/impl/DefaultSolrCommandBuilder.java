package com.coretex.searchengine.solr.server.impl;

import com.coretex.core.shell.CommandBuilder;
import com.coretex.searchengine.solr.server.SolrInstance;
import org.apache.commons.lang3.SystemUtils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultSolrCommandBuilder implements CommandBuilder {
	protected static final String UNIX_SHELL_EXECUTABLE = "bash";
	protected static final String WINDOWS_SHELL_EXECUTABLE = "cmd.exe";
	private final Map<String, String> configuration;
	private final SolrInstance solrInstance;
	private final String command;

	public DefaultSolrCommandBuilder(Map<String, String> configuration, SolrInstance solrInstance, String command) {
		this.configuration = configuration;
		this.solrInstance = solrInstance;
		this.command = command;
	}

	public Map<String, String> getConfiguration() {
		return this.configuration;
	}

	public String getCommand() {
		return this.command;
	}

	public void build(ProcessBuilder processBuilder) {
		String solrServerPath = this.configuration.get("SOLR_SERVER_PATH");
		if (SystemUtils.IS_OS_UNIX) {
			this.buildForUnix(processBuilder, solrServerPath);
		} else {
			if (!SystemUtils.IS_OS_WINDOWS) {
				throw new RuntimeException("Only Windows and Unix systems are supported");
			}

			this.buildForWindows(processBuilder, solrServerPath);
		}

	}

	public void buildForUnix(ProcessBuilder processBuilder, String solrServerPath) {
		processBuilder.directory(Paths.get(solrServerPath).toFile());
		List<String> commandParams = new ArrayList<>();
		commandParams.add(UNIX_SHELL_EXECUTABLE);
		commandParams.add(Paths.get("bin", "solr").toString());
		this.addCommand(commandParams, this.command);
		processBuilder.command().addAll(commandParams);
		processBuilder.environment().put("SOLR_SERVER_DIR", Paths.get(solrServerPath, "server").toString());
		processBuilder.environment().put("SOLR_HOME", this.solrInstance.getConfigDir());
		processBuilder.environment().put("SOLR_DATA_HOME", this.solrInstance.getDataDir());
		processBuilder.environment().put("SOLR_LOGS_DIR", this.solrInstance.getLogDir());
		processBuilder.environment().put("LOG4J_PROPS", Paths.get(this.solrInstance.getConfigDir(), "log4j2.xml").toString());
		processBuilder.environment().put("SOLR_PID_DIR", this.solrInstance.getDataDir());
	}

	public void buildForWindows(ProcessBuilder processBuilder, String solrServerPath) {
		processBuilder.directory(Paths.get(solrServerPath).toFile());
		List<String> commandParams = new ArrayList<>();
		commandParams.add(WINDOWS_SHELL_EXECUTABLE);
		commandParams.add("/C");
		commandParams.add(Paths.get("bin", "solr.cmd").toString());
		this.addCommand(commandParams, this.command);
		processBuilder.command().addAll(commandParams);
		processBuilder.environment().put("SOLR_SERVER_DIR", Paths.get(solrServerPath, "server").toString());
		processBuilder.environment().put("SOLR_HOME", this.solrInstance.getConfigDir());
		processBuilder.environment().put("SOLR_DATA_HOME", this.solrInstance.getDataDir());
		processBuilder.environment().put("SOLR_LOGS_DIR", this.solrInstance.getLogDir());
		processBuilder.environment().put("LOG4J_CONFIG", Paths.get(this.solrInstance.getConfigDir(), "log4j2.xml").toUri().toString());
	}

	protected void addCommand(List<String> commandParams, String command) {
		String[] commandParts = command.split("\\s+");
		commandParams.addAll(Arrays.asList(commandParts));
	}
}
package com.coretex.searchengine.solr.server.impl.commands;

import com.coretex.core.shell.CommandBuilder;
import com.coretex.core.shell.CommandResult;
import com.coretex.searchengine.solr.exceptions.SolrServerException;
import com.coretex.searchengine.solr.server.SolrInstance;
import com.coretex.searchengine.solr.server.SolrServerController;
import com.coretex.searchengine.solr.server.impl.DefaultSolrCommandBuilder;
import com.coretex.searchengine.solr.server.impl.SolrCommonParamsCommandBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartSolrServerCommand extends AbstractCommand {

	private static final Logger LOG = LoggerFactory.getLogger(StartSolrServerCommand.class.getName());
	protected static final String SOLR_START_COMMAND = "start";
	protected static final String SOLR_STOP_COMMAND = "stop";
	protected static final String SOLR_STATUS_COMMAND = "status";
	protected static final String RUNNING_CHECK_REGEX = "Solr process \\d+ running on port (\\d+)\\s*(\\{.*\\})?";
	protected static final String SOLR_LOG = "SOLR[%s]::[%s]";

	public void execute(Map<String, String> configuration) {
		try {
			SolrInstance solrInstance = this.getSolrInstanceForName(configuration);
			this.executeCommand(configuration, solrInstance);
		} catch (SolrServerException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	protected void executeCommand(Map<String, String> configuration, SolrInstance solrInstance) throws SolrServerException {
		getSolrServerController().creteSolrInstance();
		boolean restart = false;
		StartSolrServerCommand.ServerStatus serverStatus = this.getSolrServerStatus(configuration, solrInstance);
		if (serverStatus.getStatus().equals(StartSolrServerCommand.ServerStatus.Status.STARTED)) {
			LOG.info("Detected Solr server running on the same port for instance {}", solrInstance);
			if (isForceRestart(configuration)) {
				restart = true;
			} else if (!this.isCorrespondingServerForInstance(solrInstance, serverStatus)) {
				throw new SolrServerException(MessageFormat.format("Detected different Solr server running on the same port for instance {0}", solrInstance));
			}
		} else if (serverStatus.getStatus().equals(StartSolrServerCommand.ServerStatus.Status.UNKNOWN)) {
			LOG.info("Detected Solr server running on the same port for instance {}", solrInstance);
			if (!isForceRestart(configuration)) {
				throw new SolrServerException(MessageFormat.format("Unknown status for Solr server running on the same port for instance {0}", solrInstance));
			}

			restart = true;
		}

		if (restart) {
			this.stopSolrServer(configuration, solrInstance);
		}

		this.startSolrServer(configuration, solrInstance);

	}

	private boolean isForceRestart(Map<String, String> configuration) {
		String forceRestart = configuration.get("solrserver.forceRestart");
		return !StringUtils.isNotBlank(forceRestart) || Boolean.parseBoolean(forceRestart);
	}

	protected void startSolrServer(Map<String, String> configuration, SolrInstance solrInstance) throws SolrServerException {
		LOG.info("Starting Solr server for instance {}", solrInstance);
		Collection<CommandBuilder> commandBuilders = Arrays.asList(new DefaultSolrCommandBuilder(configuration, solrInstance, SOLR_START_COMMAND),
				new SolrCommonParamsCommandBuilder(configuration, solrInstance),
				(processBuilder) -> processBuilder.environment().put("SOLR_ULIMIT_CHECKS", Boolean.FALSE.toString()));
		CommandResult commandResult = this.getCommandExecutor().execute(commandBuilders);
		if (commandResult.getExitValue() != 0) {
			LOG.error(String.format(SOLR_LOG, SOLR_START_COMMAND, commandResult.getOutput()));
			throw new SolrServerException(MessageFormat.format("Failed to start Solr server for instance {0}", solrInstance));
		}else {
			LOG.info(String.format(SOLR_LOG, SOLR_START_COMMAND, commandResult.getOutput()));
		}
	}

	protected void stopSolrServer(Map<String, String> configuration, SolrInstance solrInstance) throws SolrServerException {
		LOG.info("Stopping Solr server for instance {}", solrInstance);
		Collection<CommandBuilder> commandBuilders = Arrays.asList(new DefaultSolrCommandBuilder(configuration, solrInstance, SOLR_STOP_COMMAND),
				new SolrCommonParamsCommandBuilder(configuration, solrInstance));
		CommandResult commandResult = this.getCommandExecutor().execute(commandBuilders);
		if (commandResult.getExitValue() != 0) {
			LOG.error(String.format(SOLR_LOG, SOLR_STOP_COMMAND, commandResult.getOutput()));
			throw new SolrServerException(MessageFormat.format("Failed to stop Solr server for instance {0}", solrInstance));
		}
	}

	protected StartSolrServerCommand.ServerStatus getSolrServerStatus(Map<String, String> configuration, SolrInstance solrInstance) {
		LOG.info("Checking Solr server status for instance {}", solrInstance);
		Collection<CommandBuilder> commandBuilders = Arrays.asList(new DefaultSolrCommandBuilder(configuration, solrInstance, SOLR_STATUS_COMMAND),
				new SolrCommonParamsCommandBuilder(configuration, solrInstance));
		CommandResult commandResult = this.getCommandExecutor().execute(commandBuilders);
		LOG.info(String.format(SOLR_LOG, SOLR_STATUS_COMMAND, commandResult.getOutput()));
		StartSolrServerCommand.ServerStatus serverStatus = new StartSolrServerCommand.ServerStatus();
		serverStatus.setPort(solrInstance.getPort());
		serverStatus.setStatus(StartSolrServerCommand.ServerStatus.Status.STOPPED);
		Pattern pattern = Pattern.compile(RUNNING_CHECK_REGEX, 32);
		Matcher matcher = pattern.matcher(commandResult.getOutput());

		while (matcher.find()) {
			String statusPort = matcher.group(1);
			String statusJson = matcher.group(2);
			if (Objects.equals(Integer.toString(solrInstance.getPort()), statusPort)) {
				serverStatus.setStatus(StartSolrServerCommand.ServerStatus.Status.UNKNOWN);
				String solrHome = this.extractSolrHome(statusJson);
				String version = this.extractVersion(statusJson);
				if (StringUtils.isNotBlank(solrHome) && StringUtils.isNotBlank(version)) {
					serverStatus.setStatus(StartSolrServerCommand.ServerStatus.Status.STARTED);
					serverStatus.setSolrHome(solrHome);
					serverStatus.setVersion(version);
				}
			}
		}

		return serverStatus;
	}

	protected String extractSolrHome(String statusJson) {
		if (StringUtils.isBlank(statusJson)) {
			return null;
		} else {
			String solrHomePrefix = "\"solr_home\":\"";
			int solrHomeStart = statusJson.indexOf(solrHomePrefix);
			int solrHomeEnd = statusJson.indexOf("\",", solrHomeStart);
			return solrHomeStart != -1 && solrHomeEnd != -1 ? statusJson.substring(solrHomeStart + solrHomePrefix.length() + 1, solrHomeEnd) : null;
		}
	}

	protected String extractVersion(String statusJson) {
		if (StringUtils.isBlank(statusJson)) {
			return null;
		} else {
			String versionPrefix = "\"version\":\"";
			int versionStart = statusJson.indexOf(versionPrefix);
			int versionEnd = statusJson.indexOf("\",", versionStart);
			return versionStart != -1 && versionEnd != -1 ? statusJson.substring(versionStart + versionPrefix.length() + 1, versionEnd) : null;
		}
	}

	protected boolean isCorrespondingServerForInstance(SolrInstance solrInstance, StartSolrServerCommand.ServerStatus serverStatus) throws SolrServerException {
		try {
			String expectedSolrHome = solrInstance.getConfigDir();
			Path expectedSolrHomePath = Paths.get(expectedSolrHome);
			String solrHome = serverStatus.getSolrHome();
			Path solrHomePath = Paths.get(solrHome);
			return Files.isSameFile(expectedSolrHomePath, solrHomePath);
		} catch (IOException ex) {
			throw new SolrServerException("Failed to check running Solr server for instance " + solrInstance, ex);
		}
	}

	@Lookup
	public SolrServerController getSolrServerController() {
		throw new UnsupportedOperationException();
	}

	protected static class ServerStatus {
		private StartSolrServerCommand.ServerStatus.Status status;
		private int port;
		private String solrHome;
		private String version;

		protected ServerStatus() {
		}

		public StartSolrServerCommand.ServerStatus.Status getStatus() {
			return this.status;
		}

		public void setStatus(StartSolrServerCommand.ServerStatus.Status status) {
			this.status = status;
		}

		public int getPort() {
			return this.port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getSolrHome() {
			return this.solrHome;
		}

		public void setSolrHome(String solrHome) {
			this.solrHome = solrHome;
		}

		public String getVersion() {
			return this.version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public enum Status {
			STARTED,
			UNKNOWN,
			STOPPED;

			Status() {
			}
		}
	}
}

package com.coretex.searchengine.solr.server.impl.commands;

import com.coretex.searchengine.solr.exceptions.SolrServerException;
import com.coretex.searchengine.solr.server.SolrInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.commons.io.FileUtils.copyDirectory;

public class CreateSolrInstanceCommand extends AbstractCommand {

	private static final Logger LOG = LoggerFactory.getLogger(CreateSolrInstanceCommand.class.getName());

	public CreateSolrInstanceCommand() {
	}

	@Override
	public void execute(Map<String, String> configuration) {
		try {
			SolrInstance solrInstance = this.getSolrInstanceForName(configuration);
			this.executeCommand(configuration, solrInstance);
		} catch (SolrServerException ex) {
			LOG.info(ex.getMessage(), ex);
		}
	}

	protected void executeCommand(Map<String, String> configuration, SolrInstance solrInstance) throws SolrServerException {
		try {
			this.createInstanceConfigDirectory(solrInstance, configuration);
			this.createInstanceDataDirectory(solrInstance);
			this.createInstanceLogDirectory(solrInstance);
		} catch (IOException ex) {
			throw new SolrServerException("Failed to create Solr instance", ex);
		}
	}

	protected void createInstanceConfigDirectory(SolrInstance solrInstance, Map<String, String> configuration) throws IOException {
		Path instanceConfigDirectory = Paths.get(solrInstance.getConfigDir());
		if (!instanceConfigDirectory.toFile().exists()) {
			LOG.info("Creating config directory ''{}'' for Solr instance {}", new Object[]{instanceConfigDirectory, solrInstance});
			Files.createDirectories(instanceConfigDirectory);
			String solrServerPath = configuration.get("SOLR_SERVER_PATH");
			List<String> configFiles = Arrays.asList("/server/resources/log4j2.xml", "/server/solr/solr.xml", "/server/solr/security.json", "/server/solr/zoo.cfg");

			for (String configFile : configFiles) {
				Path sourceConfigFilePath = Paths.get(solrServerPath, configFile);
				Path configFilePath = instanceConfigDirectory.resolve(Paths.get(StringUtils.removeEnd(configFile, ".example")).getFileName());
				Files.copy(sourceConfigFilePath, configFilePath);
			}

			Path sourceConfigSetsDirectory = Paths.get(solrServerPath, "/server/solr/configsets");
			Path configSetsDirectory = instanceConfigDirectory.resolve("configsets");
			copyDirectory(sourceConfigSetsDirectory.toFile(), configSetsDirectory.toFile());
		}

	}

	protected void createInstanceDataDirectory(SolrInstance solrInstance) throws IOException {
		Path instanceDataDirectory = Paths.get(solrInstance.getDataDir());
		if (!instanceDataDirectory.toFile().exists()) {
			LOG.info("Creating data directory ''{}'' for Solr instance {}", new Object[]{instanceDataDirectory, solrInstance});
			Files.createDirectories(instanceDataDirectory);
		}

	}

	protected void createInstanceLogDirectory(SolrInstance solrInstance) throws IOException {
		Path instanceLogDirectory = Paths.get(solrInstance.getLogDir());
		if (!instanceLogDirectory.toFile().exists()) {
			LOG.info("Creating log directory ''{}'' for Solr instance {}", new Object[]{instanceLogDirectory, solrInstance});
			Files.createDirectories(instanceLogDirectory);
		}

	}

}

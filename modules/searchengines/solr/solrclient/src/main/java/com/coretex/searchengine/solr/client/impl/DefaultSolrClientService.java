package com.coretex.searchengine.solr.client.impl;

import com.coretex.searchengine.solr.client.SolrClientService;
import com.coretex.searchengine.solr.client.factories.impl.AbstractSolrRequestFactory;
import com.coretex.searchengine.solr.client.providers.SolrClientProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class DefaultSolrClientService implements SolrClientService {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSolrClientService.class.getName());

	@Value("${solr.conf.client.type:Http2SolrClient}")
	private String solrClientType;
	@Value("${solr.conf.default.core.name:coretex_solr_core}")
	private String coreName;

	private SolrClient solrClient;

	@Resource(name = "solrUpdateRequestFactory")
	private AbstractSolrRequestFactory<Void, UpdateRequest> solrUpdateRequestFactory;

	@Resource(name = "solrJsonQueryRequestFactory")
	private AbstractSolrRequestFactory<SolrQuery, QueryRequest> solrJsonQueryRequestFactory;

	@Autowired
	private List<SolrClientProvider> solrClientProviders;

	@PostConstruct
	private void init() {
		if (CollectionUtils.isNotEmpty(solrClientProviders)) {
			solrClientProviders.stream()
					.filter(p -> p.clientType().equals(solrClientType))
					.findAny()
					.ifPresent(p -> solrClient = p.creteSolrClient());
		} else {
			LOG.error("No one solr client providers available");
		}
	}

	@Override
	public void execute(Consumer<SolrClient> solrClientConsumer) {
		if (Objects.nonNull(solrClient)) {
			solrClientConsumer.accept(solrClient);
		} else {
			LOG.error("Solr client is not available");
		}
	}

	@Override
	public <R> R execute(Function<SolrClient, R> solrClientFunction) {
		if (Objects.nonNull(solrClient)) {
			return solrClientFunction.apply(solrClient);
		} else {
			LOG.error("Solr client is not available");
			return null;
		}
	}

	@Override
	public QueryResponse query(SolrQuery query) {
		return execute(client -> {

			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Solr query: [ %s ]", query.toString()));
			}
			var queryRequest = solrJsonQueryRequestFactory.create(query);
			try {
				return queryRequest.process(client, coreName);
			} catch (IOException | SolrServerException e) {
				LOG.error(String.format("Solr query error [%s]", query.toString()), e);
				return null;
			}
		});
	}

	@Override
	public SchemaResponse schema() {
		return schema(new SchemaRequest());
	}

	@Override
	public SchemaResponse schema(SchemaRequest schemaRequest) {
		return execute(client -> {
			try {
				return schemaRequest.process(client, coreName);
			} catch (IOException | SolrServerException e) {
				LOG.error("Solr schema request error", e);
				return null;
			}
		});
	}

	@Override
	public UpdateResponse update(SolrInputDocument doc) {
		return execute(client -> {
			var updateRequest = solrUpdateRequestFactory.create(null);
			updateRequest.add(doc, true);
			try {
				return updateRequest.commit(client, coreName);
			} catch (IOException | SolrServerException e) {
				LOG.error(String.format("Solr document update error [%s]", doc.toString()), e);
				return null;
			}
		});
	}

	@Override
	public UpdateResponse update(Collection<SolrInputDocument> docs) {
		return update(docs.stream());
	}

	@Override
	public UpdateResponse update(Stream<SolrInputDocument> docs) {
		return execute(client -> {
			var updateRequest = solrUpdateRequestFactory.create(null);
			docs.forEach(doc -> updateRequest.add(doc, true));
			try {
				return updateRequest.commit(client, coreName);
			} catch (IOException | SolrServerException e) {
				LOG.error("Solr document update error", e);
				return null;
			}
		});
	}

	@Override
	public UpdateResponse deleteAll() {
		return execute(client -> {
			var updateRequest = solrUpdateRequestFactory.create(null);
			updateRequest.deleteByQuery("*:*");
			try {
				LOG.info(":: [Delete all] solr command performing :: ");
				return updateRequest.commit(client, coreName);
			} catch (IOException | SolrServerException e) {
				LOG.error("Solr document update error", e);
				return null;
			}
		});
	}

	@Override
	public void index(Stream<SolrInputDocument> docs){
		LOG.info(":: Start solr indexing :: ");
		deleteAll();
		update(docs);
		LOG.info(":: Finished solr indexing :: ");
	}

	@Override
	public void index(Collection<SolrInputDocument> docs){
		LOG.info(":: Start solr indexing :: ");
		deleteAll();
		update(docs);
		LOG.info(":: Finished solr indexing :: ");
	}
}

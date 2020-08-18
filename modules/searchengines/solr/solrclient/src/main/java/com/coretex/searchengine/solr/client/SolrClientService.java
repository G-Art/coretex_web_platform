package com.coretex.searchengine.solr.client;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.common.SolrInputDocument;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface SolrClientService{
	void execute(Consumer<SolrClient> solrClientConsumer);

	<R> R execute(Function<SolrClient, R> solrClientFunction);

	QueryResponse query(SolrQuery query);

	SchemaResponse schema();

	SchemaResponse schema(SchemaRequest schemaRequest);

	UpdateResponse update(SolrInputDocument doc);

	UpdateResponse update(Collection<SolrInputDocument> docs);

	UpdateResponse update(Stream<SolrInputDocument> docs);

	UpdateResponse deleteAll();

	void index(Stream<SolrInputDocument> docs);

	void index(Collection<SolrInputDocument> docs);
}

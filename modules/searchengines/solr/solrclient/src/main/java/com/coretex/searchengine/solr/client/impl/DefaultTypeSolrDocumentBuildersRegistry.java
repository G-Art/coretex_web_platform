package com.coretex.searchengine.solr.client.impl;

import com.coretex.searchengine.solr.client.TypeSolrDocumentBuildersRegistry;
import com.coretex.searchengine.solr.client.builders.SolrInputDocumentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DefaultTypeSolrDocumentBuildersRegistry implements TypeSolrDocumentBuildersRegistry {

	@Autowired(required = false)
	private List<SolrInputDocumentBuilder<?>> solrInputDocumentBuilderList;

	private Map<Class<?>, SolrInputDocumentBuilder<?>> solrInputDocumentBuilderMap;

	@PostConstruct
	protected void init(){
		solrInputDocumentBuilderMap = solrInputDocumentBuilderList.stream().collect(Collectors.toMap(SolrInputDocumentBuilder::getSourceType, Function.identity(),
				(solrInputDocumentBuilder, solrInputDocumentBuilder2) -> solrInputDocumentBuilder));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> SolrInputDocumentBuilder<T> findBuilderForClass(Class<T> aClass) {
		return (SolrInputDocumentBuilder<T>) solrInputDocumentBuilderMap.get(aClass);
	}
}

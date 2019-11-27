package com.coretex.core.activeorm.services.impl;

import com.coretex.core.activeorm.extractors.CoretexResultSetExtractor;
import com.coretex.core.activeorm.factories.RowMapperFactory;
import com.coretex.core.activeorm.query.operations.PageableSelectOperation;
import com.coretex.core.activeorm.query.operations.SelectOperation;
import com.coretex.core.activeorm.query.select.SelectQueryTransformationProcessor;
import com.coretex.core.activeorm.query.specs.select.PageableSelectOperationSpec;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.core.activeorm.services.SearchResult;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


public class DefaultSearchService extends AbstractJdbcService implements SearchService {

	@Autowired
	private SelectQueryTransformationProcessor transformationProcessor;

	@Autowired
	private CortexContext cortexContext;

	@Autowired
	private RowMapperFactory rowMapperFactory;

	@Override
	public <T> SearchResult<T> search(String query) {
		SelectOperationSpec<T> selectOperationSpec = new SelectOperationSpec<>(query);
		return search(selectOperationSpec);
	}

	@Override
	public <T> SearchResult<T> search(String query, Map<String, Object> parameters) {
		SelectOperationSpec<T> selectOperationSpec = new SelectOperationSpec<>(query, parameters);
		return search(selectOperationSpec);
	}

	@Override
	public <T> SearchResult<T> search(SelectOperationSpec<T> spec) {
		SelectOperation<T> selectOperation = spec.createOperation(transformationProcessor);
		selectOperation.setExtractorCreationFunction(select -> {
			CoretexResultSetExtractor<T> extractor = new CoretexResultSetExtractor<>(select, cortexContext);
			extractor.setMapperFactorySupplier(() -> rowMapperFactory);
			return extractor;
		});

		selectOperation.setJdbcTemplateSupplier(this::getJdbcTemplate);
		return new SearchResult<>(selectOperation::searchResult);
	}

	@Override
	public <T> PageableSearchResult<T> searchPageable(String query) {
		var selectOperationSpec = new PageableSelectOperationSpec<T>(query);
		return searchPageable(selectOperationSpec);
	}

	@Override
	public <T> PageableSearchResult<T> searchPageable(String query, Map<String, Object> parameters) {
		var selectOperationSpec = new PageableSelectOperationSpec<T>(query, parameters);
		return searchPageable(selectOperationSpec);
	}

	@Override
	public <T> PageableSearchResult<T> searchPageable(PageableSelectOperationSpec<T> spec) {
		PageableSelectOperation<T> selectOperation = spec.createOperation(transformationProcessor);
		selectOperation.setExtractorCreationFunction(select -> {
			CoretexResultSetExtractor<T> extractor = new CoretexResultSetExtractor<>(select, cortexContext);
			extractor.setMapperFactorySupplier(() -> rowMapperFactory);
			return extractor;
		});

		selectOperation.setJdbcTemplateSupplier(this::getJdbcTemplate);
		return new PageableSearchResult<>(selectOperation, selectOperation::searchResult);
	}

}

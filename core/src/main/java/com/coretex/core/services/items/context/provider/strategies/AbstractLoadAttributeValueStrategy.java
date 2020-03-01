package com.coretex.core.services.items.context.provider.strategies;

import com.coretex.core.activeorm.factories.RowMapperFactory;
import com.coretex.core.activeorm.query.select.SelectQueryTransformationProcessor;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.Resource;

public abstract class AbstractLoadAttributeValueStrategy implements LoadAttributeValueStrategy {

	@Resource
	private CortexContext cortexContext;

	private SelectQueryTransformationProcessor transformationProcessor;

	public AbstractLoadAttributeValueStrategy(SelectQueryTransformationProcessor transformationProcessor) {
		this.transformationProcessor = transformationProcessor;
	}

	@Lookup
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return null;
	}

	public SelectQueryTransformationProcessor getTransformationProcessor() {
		return transformationProcessor;
	}

	@Lookup
	public RowMapperFactory getRowMapperFactory() {
		return null;
	}

	public CortexContext getCortexContext() {
		return cortexContext;
	}
}

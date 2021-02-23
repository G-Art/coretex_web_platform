package com.coretex.core.activeorm.query.specs.select;

import com.coretex.core.activeorm.query.operations.contexts.SelectOperationConfigContext;
import com.coretex.core.activeorm.query.specs.SqlOperationSpec;
import com.google.common.collect.Maps;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.function.BiFunction;

public class SelectOperationSpec extends SqlOperationSpec<
		Select,
		SelectOperationSpec,
		SelectOperationConfigContext> {

	private Class<?> expectedResultType;
	private Map<String, Object> parameters;
	private BiFunction<Row, RowMetadata, ?> customMapper;
	private String transformedQuery;

	public SelectOperationSpec(String query) {
		this(query, Maps.newHashMap());
	}

	public SelectOperationSpec(String query, Map<String, Object> parameters) {
		super(query);
		this.parameters = parameters;
	}

	public SelectOperationSpec(String query, Map<String, Object> parameters, BiFunction<Row, RowMetadata, ?> customMapper) {
		this(query, parameters);
		this.customMapper = customMapper;
	}

	@Override
	public SelectOperationConfigContext createOperationContext() {
		return new SelectOperationConfigContext(this);
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void addQueryParameter(String key, Object value) {
		Assert.notNull(value, "Value is required, null given for key: " + key);
		this.parameters.put(key, value);
	}

	public void addQueryParameters(Map<String, ?> params) {
		params.forEach(this::addQueryParameter);
	}

	public Class<?> getExpectedResultType() {
		return expectedResultType;
	}

	public void setExpectedResultType(Class<?> expectedResultType) {
		this.expectedResultType = expectedResultType;
	}

	public BiFunction<Row, RowMetadata, ?> getCustomMapper() {
		return customMapper;
	}

	public String getTransformedQuery() {
		return transformedQuery;
	}

	public void setTransformedQuery(String transformedQuery) {
		this.transformedQuery = transformedQuery;
	}

	@Override
	public String toString() {
		return "query: [" + this.getQuery() + "], query parameters: [" + this.getParameters() + "]";
	}

}

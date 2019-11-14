package com.coretex.core.activeorm.query.specs.select;

import com.coretex.core.activeorm.query.operations.SelectOperation;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.specs.SqlOperationSpec;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.util.Assert;

import java.util.*;

public class SelectOperationSpec<R> extends SqlOperationSpec<Select, SelectOperation> {

	private Class<R> expectedResultType;
	private Map<String, Object> parameters;
	private Integer count = null;
	private Integer start = null;


	public SelectOperationSpec(String query) {
		this(query, Maps.newHashMap());
	}

	public SelectOperationSpec(String query, Map<String, Object> parameters) {
		super(query);
		this.parameters = parameters;
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

	public Class<R> getExpectedResultType() {
		return expectedResultType;
	}

	public void setExpectedResultType(Class<R> expectedResultType) {
		this.expectedResultType = expectedResultType;
	}

	@Override
	public SelectOperation<R> createOperation(QueryTransformationProcessor<Select> processorSupplier) {
		return new SelectOperation<>(this, processorSupplier);
	}

	@Override
	public String getQuery() {
		if(Objects.nonNull(count) || Objects.nonNull(start)){
			StringBuilder queryBuilder = new StringBuilder(super.getQuery());
			if(Objects.nonNull(count)){
				queryBuilder.append(" LIMIT ").append(count).append(" ");
			}

			if(Objects.nonNull(start)){
				queryBuilder.append(" OFFSET ").append(start).append(" ");
			}

			return queryBuilder.toString();
		}
		return super.getQuery();
	}

	@Override
	public String toString() {
		return "query: [" + this.getQuery() + "], query parameters: [" + this.getParameters() + "]";
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getStart() {
		return start;
	}
}

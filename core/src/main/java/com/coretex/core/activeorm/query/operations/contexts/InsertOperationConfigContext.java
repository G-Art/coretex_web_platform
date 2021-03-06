package com.coretex.core.activeorm.query.operations.contexts;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.InsertOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.stream.Stream;

public class InsertOperationConfigContext
		extends AbstractOperationConfigContext<Insert, InsertOperationSpec, InsertOperationConfigContext> {

	public InsertOperationConfigContext(InsertOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.INSERT;
	}

	@Override
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Stream<T> result) {
		return (R) new ReactiveSearchResult<>(() -> result);
	}


}

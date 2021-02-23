package com.coretex.core.activeorm.query.operations.contexts;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.RemoveOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import net.sf.jsqlparser.statement.delete.Delete;
import reactor.core.publisher.Flux;

public class RemoveOperationConfigContext
		extends AbstractOperationConfigContext<Delete, RemoveOperationSpec, RemoveOperationConfigContext> {

	public RemoveOperationConfigContext(RemoveOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.DELETE;
	}

	@Override
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Flux<T> result) {
		return (R) new ReactiveSearchResult<>(() -> result);
	}
}

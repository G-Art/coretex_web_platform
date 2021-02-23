package com.coretex.core.activeorm.query.operations.contexts;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.UpdateOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import net.sf.jsqlparser.statement.update.Update;
import reactor.core.publisher.Flux;

public class UpdateOperationConfigContext
		extends AbstractOperationConfigContext<Update, UpdateOperationSpec, UpdateOperationConfigContext> {

	public UpdateOperationConfigContext(UpdateOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.UPDATE;
	}

	@Override
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Flux<T> result) {
		return (R) new ReactiveSearchResult<>(() -> result);
	}
}

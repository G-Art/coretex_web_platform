package com.coretex.core.activeorm.query.operations.contexts;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import net.sf.jsqlparser.statement.Statement;
import reactor.core.publisher.Flux;

import static com.coretex.core.activeorm.query.QueryType.LOCALIZED_DATA_SAVE;

public class LocalizedDataSaveOperationConfigContext
		extends AbstractOperationConfigContext<Statement, LocalizedDataSaveOperationSpec, LocalizedDataSaveOperationConfigContext> {

	public LocalizedDataSaveOperationConfigContext(LocalizedDataSaveOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return LOCALIZED_DATA_SAVE;
	}

	@Override
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Flux<T> result) {
		return (R) new ReactiveSearchResult<>(() -> result);
	}
}

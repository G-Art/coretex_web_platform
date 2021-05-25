package com.coretex.core.activeorm.query.operations.contexts;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.LocalizedDataRemoveOperation;
import com.coretex.core.activeorm.query.specs.LocalizedDataRemoveOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import net.sf.jsqlparser.statement.delete.Delete;

import java.util.stream.Stream;

public class LocalizedDataRemoveOperationConfigContext
		extends AbstractOperationConfigContext<Delete, LocalizedDataRemoveOperationSpec, LocalizedDataRemoveOperationConfigContext> {

	public LocalizedDataRemoveOperationConfigContext(LocalizedDataRemoveOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_DELETE;
	}

	@Override
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Stream<T> result) {
		return null;
	}
}

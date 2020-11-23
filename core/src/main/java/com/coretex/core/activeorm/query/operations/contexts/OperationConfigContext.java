package com.coretex.core.activeorm.query.operations.contexts;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.SqlOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import net.sf.jsqlparser.statement.Statement;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface OperationConfigContext<
		S extends Statement,
		OS extends SqlOperationSpec<S, OS, CTX>,
		CTX extends OperationConfigContext<S, OS, CTX>> {

	OS getOperationSpec();

	Supplier<String> getQuerySupplier();

	QueryType getQueryType();

	<R extends ReactiveSearchResult<T>, T> R wrapResult(Stream<T> result);
}

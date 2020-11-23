package com.coretex.core.activeorm.query.operations.contexts;

import com.coretex.core.activeorm.query.specs.SqlOperationSpec;
import net.sf.jsqlparser.statement.Statement;

import java.util.function.Supplier;

public abstract class AbstractOperationConfigContext<
		S extends Statement,
		OS extends SqlOperationSpec<S, OS, CTX>,
		CTX extends OperationConfigContext<S, OS, CTX>>
		implements OperationConfigContext<S,OS, CTX> {

	private final OS operationSpec;
	private final Supplier<String> querySupplier;

	public AbstractOperationConfigContext(OS operationSpec) {
		this.operationSpec = operationSpec;
		var query = operationSpec.getQuery();
		this.querySupplier = () -> query;
	}

	@Override
	public OS getOperationSpec() {
		return operationSpec;
	}

	@Override
	public Supplier<String> getQuerySupplier() {
		return querySupplier;
	}

}

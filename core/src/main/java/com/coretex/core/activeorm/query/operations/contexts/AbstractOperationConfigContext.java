package com.coretex.core.activeorm.query.operations.contexts;

import com.coretex.core.activeorm.query.specs.SqlOperationSpec;
import net.sf.jsqlparser.statement.Statement;

public abstract class AbstractOperationConfigContext<
		S extends Statement,
		OS extends SqlOperationSpec<S>>
		implements OperationConfigContext<S,OS> {

	private final OS operationSpec;

	public AbstractOperationConfigContext(OS operationSpec) {
		this.operationSpec = operationSpec;
	}

	@Override
	public OS getOperationSpec() {
		return operationSpec;
	}


}

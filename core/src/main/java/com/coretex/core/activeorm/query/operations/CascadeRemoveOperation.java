package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.RemoveOperationSpec;

import static com.coretex.core.general.utils.ItemUtils.isSystemType;

public class CascadeRemoveOperation extends RemoveOperation {

	public CascadeRemoveOperation(RemoveOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.DELETE;
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}


}

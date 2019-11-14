package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.InsertOperationSpec;
import com.coretex.core.activeorm.query.specs.UpdateOperationSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CascadeUpdateOperation extends UpdateOperation {

	private Logger LOG = LoggerFactory.getLogger(CascadeUpdateOperation.class);

	public CascadeUpdateOperation(UpdateOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.UPDATE;
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}
}

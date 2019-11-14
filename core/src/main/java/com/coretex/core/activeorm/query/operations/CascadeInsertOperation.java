package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.InsertOperationSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CascadeInsertOperation extends InsertOperation {

	private Logger LOG = LoggerFactory.getLogger(CascadeInsertOperation.class);

	public CascadeInsertOperation(InsertOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.INSERT;
	}


	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}
}

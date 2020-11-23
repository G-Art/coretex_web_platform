package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;

public class CascadeRemoveOperation extends RemoveOperation {


	public CascadeRemoveOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.DELETE_CASCADE;
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}


}

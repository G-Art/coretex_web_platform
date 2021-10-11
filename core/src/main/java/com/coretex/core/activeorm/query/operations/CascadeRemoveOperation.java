package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.core.services.bootstrap.dialect.DbDialectService;

public class CascadeRemoveOperation extends RemoveOperation {


	public CascadeRemoveOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService, DbDialectService dbDialectService) {
		super(abstractJdbcService, itemOperationInterceptorService, dbDialectService);
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

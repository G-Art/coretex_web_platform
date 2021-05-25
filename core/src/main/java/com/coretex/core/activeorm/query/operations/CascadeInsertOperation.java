package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CascadeInsertOperation extends InsertOperation {

	private Logger LOG = LoggerFactory.getLogger(CascadeInsertOperation.class);

	public CascadeInsertOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService,itemOperationInterceptorService);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.INSERT_CASCADE;
	}


	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}
}

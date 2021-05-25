package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.LocalizedDataRemoveOperationConfigContext;
import com.coretex.core.activeorm.query.specs.LocalizedDataRemoveOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import net.sf.jsqlparser.statement.delete.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalizedDataRemoveOperation extends ModificationOperation<Delete, LocalizedDataRemoveOperationSpec, LocalizedDataRemoveOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(LocalizedDataRemoveOperation.class);

	public LocalizedDataRemoveOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService);
	}

	@Override
	protected void executeBefore(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		//not required
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_DELETE;
	}

	@Override
	public void executeOperation(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		var query = operationConfigContext.getQuerySupplier().get();
		if(LOG.isDebugEnabled()){
			LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
		}
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(query, operationConfigContext.getOperationSpec().getParams()));
	}

	@Override
	protected void executeAfter(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		//not required
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}

	@Override
	protected boolean useInterceptors(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		return false;
	}
}

package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.LocalizedDataRemoveOperationSpec;
import net.sf.jsqlparser.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalizedDataRemoveOperation extends ModificationOperation<Statement, LocalizedDataRemoveOperationSpec> {

	private Logger LOG = LoggerFactory.getLogger(LocalizedDataRemoveOperation.class);

	public LocalizedDataRemoveOperation(LocalizedDataRemoveOperationSpec operationSpec) {
		super(operationSpec);
	}

	@Override
	protected void executeBefore() {
		//not required
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_DELETE;
	}

	@Override
	public void executeOperation() {
		var query = getQuery();
		if(LOG.isDebugEnabled()){
			LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
		}
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(query, getOperationSpec().getParams()));
	}

	@Override
	protected void executeAfter() {
		//not required
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}
}

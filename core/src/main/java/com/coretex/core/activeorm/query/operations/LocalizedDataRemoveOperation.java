package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.LocalizedDataRemoveOperationSpec;
import com.coretex.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import net.sf.jsqlparser.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
		executeJdbcOperation(jdbcTemplate -> jdbcTemplate.update(getStatement().toString(), getOperationSpec().getParams()));
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

package com.coretex.core.activeorm.query;

import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdleQueryTransformationProcessor implements QueryTransformationProcessor<QueryStatementContext<Select>> {

	private Logger LOG = LoggerFactory.getLogger(IdleQueryTransformationProcessor.class);

	@Override
	public void transform(QueryStatementContext<Select> statement) {
		if(LOG.isDebugEnabled()){
			LOG.debug(String.format("Statement query [%s]", statement.getStatement()));
		}
		// no action is required for statement
	}
}

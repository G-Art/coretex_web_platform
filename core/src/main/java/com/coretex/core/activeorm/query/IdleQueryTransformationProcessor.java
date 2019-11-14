package com.coretex.core.activeorm.query;

import net.sf.jsqlparser.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdleQueryTransformationProcessor implements QueryTransformationProcessor<Statement> {

	private Logger LOG = LoggerFactory.getLogger(IdleQueryTransformationProcessor.class);

	@Override
	public void transform(Statement statement) {
		if(LOG.isDebugEnabled()){
			LOG.debug(String.format("Statement query [%s]", statement));
		}
		// no action is required for statement
	}
}

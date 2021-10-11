package com.coretex.core.services.items.context.provider.strategies;

import com.coretex.core.activeorm.factories.RowMapperFactory;
import com.coretex.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.core.services.bootstrap.impl.CoretexContext;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.Resource;

public abstract class AbstractLoadAttributeValueStrategy implements LoadAttributeValueStrategy {

	@Resource
	private CoretexContext coretexContext;

	@Resource
	private DbDialectService dbDialectService;

	@Lookup
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return null;
	}

	@Lookup
	public RowMapperFactory getRowMapperFactory() {
		return null;
	}
	@Lookup
	public ActiveOrmOperationExecutor getOperationExecutor() {
		return null;
	}

	public CoretexContext getCortexContext() {
		return coretexContext;
	}

	public DbDialectService getDbDialectService() {
		return dbDialectService;
	}
}

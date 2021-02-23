package com.coretex.core.services.items.context.provider.strategies;

import com.coretex.core.activeorm.factories.RowMapperFactory;
import com.coretex.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import org.springframework.beans.factory.annotation.Lookup;

import javax.annotation.Resource;

public abstract class AbstractLoadAttributeValueStrategy implements LoadAttributeValueStrategy {

	@Resource
	private CortexContext cortexContext;

	@Lookup
	public RowMapperFactory getRowMapperFactory() {
		return null;
	}
	@Lookup
	public ActiveOrmOperationExecutor getOperationExecutor() {
		return null;
	}

	public CortexContext getCortexContext() {
		return cortexContext;
	}
}

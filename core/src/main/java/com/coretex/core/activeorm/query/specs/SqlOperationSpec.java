package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.factories.OperationFactory;
import com.coretex.core.activeorm.query.operations.SqlOperation;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.server.ApplicationContextProvider;
import net.sf.jsqlparser.statement.Statement;

import java.util.Optional;
import java.util.function.Supplier;


public abstract class SqlOperationSpec<S extends Statement, O extends SqlOperation> {

	private OperationFactory operationFactory;
	private AbstractJdbcService abstractJdbcService;
	private CortexContext cortexContext;
	private Optional<String> query;
	private Supplier<String> querySupplier = () -> {
		throw new IllegalArgumentException("No query is defined");
	};

	public SqlOperationSpec(String query) {
		this.query = Optional.ofNullable(query);
		cortexContext = ApplicationContextProvider.getApplicationContext().getBean(CortexContext.class);
	}

	protected SqlOperationSpec() {
		this(null);
	}

	public String getQuery(){
		if (query.isEmpty()){
			String newQuery = query.orElseGet(querySupplier);
			query = Optional.of(newQuery);
		}
		return query.get();
	}

	public CortexContext getCortexContext() {
		return cortexContext;
	}

	public void setQuerySupplier(Supplier<String> querySupplier){
		this.querySupplier = querySupplier;
	}

	public abstract O createOperation(QueryTransformationProcessor<S> processor);

	public OperationFactory getOperationFactory() {
		return operationFactory;
	}

	public void setOperationFactory(OperationFactory operationFactory) {
		this.operationFactory = operationFactory;
	}

	public AbstractJdbcService getJdbcService() {
		return abstractJdbcService;
	}

	public void setJdbcService(AbstractJdbcService abstractJdbcService) {
		this.abstractJdbcService = abstractJdbcService;
	}
}

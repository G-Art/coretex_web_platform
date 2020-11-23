package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.operations.contexts.OperationConfigContext;
import net.sf.jsqlparser.statement.Statement;

import java.util.Optional;
import java.util.function.Supplier;


public abstract class SqlOperationSpec<
		S extends Statement,
		OS extends SqlOperationSpec<S, OS, CTX>,
		CTX extends OperationConfigContext<S, OS, CTX>> {

	private boolean nativeQuery = false;
	private Optional<String> query;
	private Supplier<String> querySupplier = () -> {
		throw new IllegalArgumentException("No query is defined");
	};

	public SqlOperationSpec(String query) {
		this.query = Optional.ofNullable(query);
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

	public boolean isNativeQuery() {
		return nativeQuery;
	}

	protected void setNativeQuery(boolean nativeQuery) {
		this.nativeQuery = nativeQuery;
	}


	public void setQuerySupplier(Supplier<String> querySupplier){
		this.querySupplier = querySupplier;
	}

	public abstract CTX createOperationContext();

}

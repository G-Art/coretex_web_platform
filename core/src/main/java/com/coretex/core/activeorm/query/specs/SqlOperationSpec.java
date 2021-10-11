package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.operations.contexts.OperationConfigContext;
import net.sf.jsqlparser.statement.Statement;


public abstract class SqlOperationSpec<
		S extends Statement> {

	private boolean nativeQuery = false;

	public SqlOperationSpec() {
	}

	public boolean isNativeQuery() {
		return nativeQuery;
	}

	protected void setNativeQuery(boolean nativeQuery) {
		this.nativeQuery = nativeQuery;
	}

	public abstract <CTX extends OperationConfigContext<?,?>> CTX createOperationContext();

}

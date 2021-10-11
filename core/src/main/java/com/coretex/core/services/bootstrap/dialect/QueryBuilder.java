package com.coretex.core.services.bootstrap.dialect;

import com.coretex.core.activeorm.query.operations.contexts.OperationConfigContext;

public interface QueryBuilder<CTX extends OperationConfigContext<?,?>> {

	default String buildQuery(CTX context){
		prepareValueData(context);
		return createQuery(context);
	}

	String createQuery(CTX context);

	void prepareValueData(CTX context);

}

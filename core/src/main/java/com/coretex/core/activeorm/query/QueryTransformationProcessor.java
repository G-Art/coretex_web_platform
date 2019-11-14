package com.coretex.core.activeorm.query;

import net.sf.jsqlparser.statement.Statement;


public interface QueryTransformationProcessor<S extends Statement> {

	void transform(S statement);
}

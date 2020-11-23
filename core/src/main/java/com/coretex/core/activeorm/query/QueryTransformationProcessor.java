package com.coretex.core.activeorm.query;

import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import net.sf.jsqlparser.statement.Statement;


public interface QueryTransformationProcessor <S extends QueryInfoHolder<? extends Statement>> {

	void transform(S statement);
}

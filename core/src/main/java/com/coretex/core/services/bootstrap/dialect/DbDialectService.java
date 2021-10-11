package com.coretex.core.services.bootstrap.dialect;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.items.core.RegularTypeItem;

import java.util.Date;

public interface DbDialectService {
	Integer getSqlTypeId(String sqlTypeName);

	String dateToString(Date date);

	Date stringToDate(String date);

	QueryBuilder<? extends OperationConfigContext<?,?>> getQueryBuilder(QueryType queryType);

	String getSqlTypeName(RegularTypeItem regularTypeItem);
}

package com.coretex.core.services.bootstrap;

import java.util.Date;

public interface DbDialectService {
	Integer getSqlTypeId(String sqlTypeName);

	String dateToString(Date date);

	Date stringToDate(String date);
}

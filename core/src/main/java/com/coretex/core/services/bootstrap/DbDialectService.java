package com.coretex.core.services.bootstrap;

import java.util.Date;

public interface DbDialectService {

	String dateToString(Date date);

	Date stringToDate(String date);
}

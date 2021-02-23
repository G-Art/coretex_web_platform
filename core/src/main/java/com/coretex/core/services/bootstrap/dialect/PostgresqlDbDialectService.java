package com.coretex.core.services.bootstrap.dialect;

import com.coretex.core.services.bootstrap.DbDialectService;
import org.springframework.r2dbc.core.DatabaseClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostgresqlDbDialectService implements DbDialectService {

	private DatabaseClient databaseClient;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	protected PostgresqlDbDialectService(DatabaseClient databaseClient) {
		this.databaseClient = databaseClient;
	}

	@Override
	public String dateToString(Date date) {
		return formatter.format(date);
	}

	@Override
	public Date stringToDate(String date) {
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(String.format("Parse date exception [%s]", date), e);
		}
	}
}

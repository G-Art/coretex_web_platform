package com.coretex.core.services.bootstrap.dialect;

import com.coretex.core.services.bootstrap.DbDialectService;

import javax.sql.DataSource;

public class DbDialectFactory {
	public DbDialectService getDialectService(DataSource defaultDataSource, String dialect) {
		switch (dialect.toUpperCase()){
			case "ORACLE" : {
				throw new UnsupportedOperationException("Oracle dialect is not supported yet");
			}

			case "MYSQL" : {
				throw new UnsupportedOperationException("MySql dialect is not supported yet");
			}

			case "POSTGRESQL":
			default:{
				return new PostgresqlDbDialectService(defaultDataSource);
			}
		}
	}
}

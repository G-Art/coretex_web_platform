package com.coretex.core.services.bootstrap.dialect;

import com.coretex.core.services.bootstrap.DbDialectService;
import com.coretex.core.services.bootstrap.dialect.postgres.PostgresCodecRegistrar;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.PostgresqlConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ConnectionFactoryProvider;
import org.springframework.r2dbc.core.DatabaseClient;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ServiceLoader;

public class DbDialectFactory {

	public DbDialectService getDialectService(DatabaseClient databaseClient, String dialect) {
		switch (dialect.toUpperCase()) {
			case "ORACLE" -> {
				throw new UnsupportedOperationException("Oracle dialect is not supported yet");
			}
			case "MYSQL" -> {
				throw new UnsupportedOperationException("MySql dialect is not supported yet");
			}
			default -> {
				return new PostgresqlDbDialectService(databaseClient);
			}
		}
	}


	public ConnectionFactory getConnectionFactory(String dialect, ConnectionFactoryOptions connectionFactoryOptions){
		switch (dialect.toUpperCase()) {
			case "ORACLE" -> {
				throw new UnsupportedOperationException("Oracle dialect is not supported yet");
			}
			case "MYSQL" -> {
				throw new UnsupportedOperationException("MySql dialect is not supported yet");
			}
			default -> {
				var builder = PostgresqlConnectionFactoryProvider.builder(connectionFactoryOptions);
				builder.codecRegistrar(new PostgresCodecRegistrar());
				return new PostgresqlConnectionFactory(builder.build());
			}
		}
	}

	private static ServiceLoader<ConnectionFactoryProvider> loadProviders() {
		return AccessController.doPrivileged((PrivilegedAction<ServiceLoader<ConnectionFactoryProvider>>) () -> ServiceLoader.load(ConnectionFactoryProvider.class,
				ConnectionFactoryProvider.class.getClassLoader()));
	}

}

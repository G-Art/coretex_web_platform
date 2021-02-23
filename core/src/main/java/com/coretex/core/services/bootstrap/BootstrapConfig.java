package com.coretex.core.services.bootstrap;

import com.coretex.core.services.bootstrap.dialect.DbDialectFactory;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.core.services.bootstrap.meta.MetaCollector;
import com.coretex.core.services.bootstrap.meta.MetaDataExtractor;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.pool.PoolingConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class BootstrapConfig {

	public static final String ITEM_CONTEXT_FACTORY_QUALIFIER = "defaultItemContextFactory";

	public static final String META_TYPE_PROVIDER_QUALIFIER = "defaultMetaTypeProvider";

	public static final String BOOTSTRAP_CONTEXT_QUALIFIER = "defaultBootstrapContext";

	public static final String DATASOURCE_QUALIFIER = "dataSource";
	public static final String DATABASE_CLIENT_QUALIFIER = "databaseClient";

	@Value("${db.driver.class.name}")
	private String driverClassName;

	@Value("${db.url}")
	private String dbUrl;

	@Value("${db.url.reactive}")
	private String dbUrlReactive;

	@Value("${db.user.name}")
	private String userName;

	@Value("${db.user.password}")
	private String password;

	@Value("${db.dialect}")
	private String dialect;

	private ConnectionFactory connectionFactory;

	private final DbDialectFactory dialectFactory = new DbDialectFactory();

	@PostConstruct
	private void init() {

        ConnectionPoolConfiguration.Builder builder = ConnectionPoolConfiguration.builder(
                dialectFactory.getConnectionFactory(dialect, ConnectionFactoryOptions
				.parse(dbUrlReactive)
				.mutate()
				.option(PoolingConnectionFactoryProvider.MAX_IDLE_TIME, Duration.of(1, ChronoUnit.MINUTES))
				.option(PoolingConnectionFactoryProvider.INITIAL_SIZE, 10)
				.option(PoolingConnectionFactoryProvider.MAX_SIZE, 100)
				.build()
		));

        connectionFactory = new ConnectionPool(builder.build());
	}

	@Bean(DATABASE_CLIENT_QUALIFIER)
	public DatabaseClient databaseClient() {
		return DatabaseClient.create(connectionFactory);
	}

	@Bean
	public ReactiveTransactionManager reactiveTransactionManager() {
		return new R2dbcTransactionManager(connectionFactory);
	}

	@Bean
	public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
		return TransactionalOperator.create(transactionManager);
	}

	@Bean
	public DbDialectService dbDialectService() {
		return dialectFactory.getDialectService(databaseClient(), dialect);
	}

	@Bean
	protected MetaDataExtractor metaDataExtractor() {
		MetaDataExtractor extractor = new MetaDataExtractor();
		extractor.setDatabaseClient(databaseClient());
		return extractor;
	}

	@Bean
	protected MetaCollector metaCollector(@Autowired MetaDataExtractor metaDataExtractor) {
		MetaCollector collector = new MetaCollector();
		collector.setExtractor(metaDataExtractor);
		return collector;
	}

	@Bean(name = {META_TYPE_PROVIDER_QUALIFIER, BOOTSTRAP_CONTEXT_QUALIFIER})
	public CortexContext cortexContext(@Autowired MetaCollector metaCollector) {
		CortexContext cortexContext = new CortexContext();
		cortexContext.setMetaCollector(metaCollector);
		cortexContext.setDbDialectService(dbDialectService());
		return cortexContext;
	}
}

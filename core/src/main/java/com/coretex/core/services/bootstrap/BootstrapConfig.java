package com.coretex.core.services.bootstrap;

import com.coretex.core.services.bootstrap.dialect.DbDialectFactory;
import com.coretex.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.core.services.bootstrap.dialect.QueryTransformationHints;
import com.coretex.core.services.bootstrap.impl.CoretexContext;
import com.coretex.core.services.bootstrap.meta.MetaCollector;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class BootstrapConfig {

    public static final String ITEM_CONTEXT_FACTORY_QUALIFIER = "defaultItemContextFactory";

    public static final String META_TYPE_PROVIDER_QUALIFIER = "defaultMetaTypeProvider";

    public static final String BOOTSTRAP_CONTEXT_QUALIFIER = "defaultBootstrapContext";

    public static final String DATASOURCE_QUALIFIER = "dataSource";

    @Value("${db.driver.class.name}")
    private String driverClassName;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.user.name}")
    private String userName;

    @Value("${db.user.password}")
    private String password;

    @Value("${db.dialect}")
    private String dialect;

    private DriverManagerDataSource dataSource;

    private DbDialectFactory dialectFactory = new DbDialectFactory();

    @PostConstruct
    private void init(){
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
    }

    protected DataSource getDataSource(){
        return dataSource;
    }

    @Bean(DATASOURCE_QUALIFIER)
    public DataSource defaultDataSource() {
//        props.put("dataSource.logWriter", new PrintWriter(System.out));

        HikariConfig config = new HikariConfig();
        config.setDataSource(getDataSource());
        return new HikariDataSource(config);
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(defaultDataSource());
    }

    @Bean
    @Scope("prototype")
    public TransactionTemplate transactionTemplate(){
        return new TransactionTemplate(transactionManager());
    }

    @Bean
    public DbDialectService dbDialectService(){
        return dialectFactory.getDialectService(dataSource, dialect);
    }

    @Bean
    protected QueryTransformationHints queryTransformationHints() {
        return dialectFactory.getQueryTransformationHints(dialect);
    }

    @Bean
    protected MetaCollector metaCollector(@Autowired NamedParameterJdbcTemplate jdbcTemplate) {
        MetaCollector collector = new MetaCollector();
        collector.setExtractor(dialectFactory.getMetaDataExtractor(jdbcTemplate, dialect));
        return collector;
    }

    @Bean(name = {META_TYPE_PROVIDER_QUALIFIER, BOOTSTRAP_CONTEXT_QUALIFIER})
    public CoretexContext coretexContext(@Autowired MetaCollector metaCollector) {
        CoretexContext coretexContext = new CoretexContext();
        coretexContext.setMetaCollector(metaCollector);
        coretexContext.setDbDialectService(dbDialectService());
        return coretexContext;
    }

    @Bean("jdbcTemplate")
//    @Scope("prototype")
    public NamedParameterJdbcTemplate newJdbcTemplate(@Autowired DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }

}

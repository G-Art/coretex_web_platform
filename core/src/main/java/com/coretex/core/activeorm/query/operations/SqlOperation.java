package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.exceptions.QueryException;
import com.coretex.core.activeorm.factories.OperationFactory;
import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.SqlOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class SqlOperation<S extends Statement, O extends SqlOperationSpec<S, ? extends SqlOperation>> {

	private O operationSpec;
	private S statement;

	private Supplier<NamedParameterJdbcTemplate> jdbcTemplateSupplier;

	public SqlOperation(O operationSpec) {
		this.operationSpec = operationSpec;
		this.statement = parseQuery(operationSpec.getQuery());
	}

	public O getOperationSpec() {
		return operationSpec;
	}

	protected S parseQuery(String query){
		try {
			return (S) CCJSqlParserUtil.parse(query);
		} catch (JSQLParserException e) {
			throw new QueryException(String.format("Query parsing error [%s]", query), e);
		}
	}

	protected S getStatement(){
		return statement;
	}

	public abstract QueryType getQueryType();

	public abstract void execute();

	public NamedParameterJdbcTemplate getJdbcTemplate(){
		return jdbcTemplateSupplier.get();
	}

	protected void executeJdbcOperation(Consumer<NamedParameterJdbcTemplate> jdbcTemplateConsumer){
		jdbcTemplateConsumer.accept(jdbcTemplateSupplier.get());
	}

	protected AbstractJdbcService getJdbcService(){
		return getOperationSpec().getJdbcService();
	}

	public void setJdbcTemplateSupplier(Supplier<NamedParameterJdbcTemplate> jdbcTemplateSupplier) {
		this.jdbcTemplateSupplier = jdbcTemplateSupplier;
	}

	public OperationFactory getOperationFactory(){
		return getOperationSpec().getOperationFactory();
	}

}

package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.core.activeorm.query.specs.SqlOperationSpec;
import net.sf.jsqlparser.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class SqlOperation<
		S extends Statement,
		OS extends SqlOperationSpec<S, OS, CTX>,
		CTX extends OperationConfigContext<S, OS, CTX>> {

	private Logger LOG = LoggerFactory.getLogger(SqlOperation.class);

	public abstract QueryType getQueryType();

	public abstract <T> Flux<T> execute(CTX operationConfigContext);

	@Lookup
	public DatabaseClient getDatabaseClient() {
		return null;
	}

	protected Mono<Integer> executeReactiveOperation(Function<DatabaseClient, Mono<Integer>> databaseClientConsumer){
		return Mono.fromSupplier(()->databaseClientConsumer.apply(getDatabaseClient()))
				.flatMap(Function.identity());
	}

	protected <K,V> DatabaseClient.GenericExecuteSpec bindForEach(DatabaseClient.GenericExecuteSpec sql, Map<K,V> params, BiFunction<DatabaseClient.GenericExecuteSpec, Map.Entry<K,V>, DatabaseClient.GenericExecuteSpec> bindFunction){
		var $sql = sql;
		for (Map.Entry<K, V> kvEntry : params.entrySet()) {
			$sql = bindFunction.apply($sql, kvEntry);
		}
		return $sql;
	}

}

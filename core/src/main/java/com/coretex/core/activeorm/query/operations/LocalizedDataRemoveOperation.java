package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.LocalizedDataRemoveOperationConfigContext;
import com.coretex.core.activeorm.query.specs.LocalizedDataRemoveOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import net.sf.jsqlparser.statement.delete.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class LocalizedDataRemoveOperation extends ModificationOperation<Delete, LocalizedDataRemoveOperationSpec, LocalizedDataRemoveOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(LocalizedDataRemoveOperation.class);

	public LocalizedDataRemoveOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService);
	}

	@Override
	protected Mono<Integer> executeBefore(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		//not required
		return Mono.just(0);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_DELETE;
	}

	@Override
	public Mono<Integer> executeOperation(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		var query = operationConfigContext.getQuerySupplier().get();
		if(LOG.isDebugEnabled()){
			LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
		}
		return executeReactiveOperation(databaseClient -> {
			var sql = bindForEach(
					databaseClient.sql(query),
					operationConfigContext.getOperationSpec().getParams(),
					(spec, entry) -> spec.bind(entry.getKey(), entry.getValue())
			);
			return sql.fetch().rowsUpdated();
		});
	}

	@Override
	protected Mono<Integer> executeAfter(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		//not required
		return Mono.just(0);
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}

	@Override
	protected boolean useInterceptors(LocalizedDataRemoveOperationConfigContext operationConfigContext) {
		return false;
	}
}

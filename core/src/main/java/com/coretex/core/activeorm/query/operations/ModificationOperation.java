package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.core.activeorm.query.specs.ModificationOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import net.sf.jsqlparser.statement.Statement;
import org.springframework.beans.factory.annotation.Lookup;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Supplier;

public abstract class ModificationOperation<
		S extends Statement,
		OS extends ModificationOperationSpec<S, OS, CTX>,
		CTX extends OperationConfigContext<S, OS, CTX>>
		extends SqlOperation<S, OS, CTX> {

	private AbstractJdbcService abstractJdbcService;
	private ItemOperationInterceptorService itemOperationInterceptorService;

	public ModificationOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService) {
		this.abstractJdbcService = abstractJdbcService;
		this.itemOperationInterceptorService = itemOperationInterceptorService;
	}

	@Override
	public <T> Flux<T> execute(CTX operationConfigContext) {
		if (useInterceptors(operationConfigContext)) {
			onPrepare(operationConfigContext);
		}
		return (Flux<T>) Flux.just(doTransactional(operationConfigContext, () -> Flux.just(
				executeBefore(operationConfigContext)
						.subscribeOn(Schedulers.boundedElastic())
						.publishOn(Schedulers.boundedElastic()),
				executeOperation(operationConfigContext)
						.subscribeOn(Schedulers.boundedElastic())
						.publishOn(Schedulers.boundedElastic()),
				executeAfter(operationConfigContext)
						.subscribeOn(Schedulers.boundedElastic())
						.publishOn(Schedulers.boundedElastic())
		)));
	}


	protected Mono<Integer> doTransactional(CTX operationConfigContext, Supplier<Flux<Mono<Integer>>> executor) {
		var operationSpec = operationConfigContext.getOperationSpec();
		if (isTransactionInitiator() && operationSpec.isTransactionEnabled()) {
			return getJdbcService().executeInReactiveTransaction(tx -> executor.get().onErrorResume(throwable -> {
				tx.setRollbackOnly();
				return Flux.error(throwable);
			}));
		} else {
			return Flux.concat(executor.get()).reduce(0, Integer::sum);
		}
	}

	@Lookup
	protected ActiveOrmOperationExecutor getActiveOrmOperationExecutor() {
		return null;
	}

	protected AbstractJdbcService getJdbcService() {
		return abstractJdbcService;
	}

	protected void onPrepare(CTX operationConfigContext) {
		itemOperationInterceptorService.onSavePrepare(operationConfigContext.getOperationSpec().getItem());
	}

	protected boolean useInterceptors(CTX operationConfigContext) {
		return true;
	}

	protected ItemOperationInterceptorService getItemOperationInterceptorService() {
		return itemOperationInterceptorService;
	}

	protected abstract boolean isTransactionInitiator();

	protected abstract Mono<Integer> executeBefore(CTX operationConfigContext);

	public abstract Mono<Integer> executeOperation(CTX operationConfigContext);

	protected abstract Mono<Integer> executeAfter(CTX operationConfigContext);


}

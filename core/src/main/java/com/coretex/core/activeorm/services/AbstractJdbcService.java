package com.coretex.core.activeorm.services;

import com.coretex.core.activeorm.query.ActiveOrmOperationExecutor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public abstract class AbstractJdbcService {


	@Lookup
	public TransactionalOperator getTransactionalOperator(){ return null;}

	@Lookup
	public ActiveOrmOperationExecutor getOrmOperationExecutor(){
		return null;
	}

	public Mono<Integer> executeInReactiveTransaction(Function<ReactiveTransaction, Flux<Mono<Integer>>> transactionFluxFunction) {
		return Flux.concat(getTransactionalOperator()
				.execute(transactionFluxFunction::apply))
				.reduce(0, Integer::sum);
	}
}

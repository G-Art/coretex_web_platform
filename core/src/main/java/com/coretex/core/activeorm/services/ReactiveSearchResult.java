package com.coretex.core.activeorm.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

public class ReactiveSearchResult<T> {

	private Logger LOG = LoggerFactory.getLogger(ReactiveSearchResult.class);

	private Flux<T> result;

	public ReactiveSearchResult(Supplier<Flux<T>> resultSupplier) {
		this.result = resultSupplier.get();
	}

	public Flux<T> getResultStream() {
		return result;
	}
}

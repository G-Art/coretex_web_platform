package com.coretex.core.activeorm.cache.impl;

import com.coretex.core.activeorm.cache.CacheValueRetrievingStrategy;
import com.coretex.core.activeorm.cache.CacheValueStoringStrategy;
import org.apache.commons.lang3.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CachedFluxValueStoringStrategy<T> implements
		CacheValueStoringStrategy<List<Signal<T>>, List<T>>,
		CacheValueRetrievingStrategy<Mono<List<Signal<T>>>, List<T>> {

	private Function<T, T> outModification = t -> {
		if (t instanceof Cloneable) {
			return ObjectUtils.cloneIfPossible(t);
		}
		return t;
	};

	public CachedFluxValueStoringStrategy() {
	}

	public CachedFluxValueStoringStrategy(Function<T, T> outModification) {
		this.outModification = outModification;
	}

	@Override
	public List<T> storeIncome(List<Signal<T>> result) {
		return result.stream().map(Signal::get)
				.filter(Objects::nonNull)
				.map(outModification).collect(Collectors.toList());
	}

	@Override
	public Mono<List<Signal<T>>> storeOutcome(List<T> result) {
		if(Objects.isNull(result)){
			return Mono.empty();
		}
		return Flux.fromIterable(result)
				.map(outModification)
				.materialize()
				.collectList();
	}

}

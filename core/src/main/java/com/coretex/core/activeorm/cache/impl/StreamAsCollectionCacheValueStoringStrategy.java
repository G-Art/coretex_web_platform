package com.coretex.core.activeorm.cache.impl;

import com.coretex.core.activeorm.cache.CacheValueStoringStrategy;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StreamAsCollectionCacheValueStoringStrategy<T, C extends Collection<T>> implements CacheValueStoringStrategy<Stream<T>, C> {

	@Override
	public C storeIncome(Stream<T> stream) {
		return stream.collect(Collectors.<T, C>toCollection(this::createCollection));
	}

	@Override
	public Stream<T> storeOutcome(C value) {
		return value.stream();
	}

	public abstract C createCollection();
}

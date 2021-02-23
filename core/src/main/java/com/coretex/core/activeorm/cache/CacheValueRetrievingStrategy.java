package com.coretex.core.activeorm.cache;

public interface CacheValueRetrievingStrategy<V, R> {
	V storeOutcome(R value);
}

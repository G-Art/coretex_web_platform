package com.coretex.core.activeorm.cache;

public interface CacheValueStoringStrategy<V, R> {
	R storeIncome(V value);

}

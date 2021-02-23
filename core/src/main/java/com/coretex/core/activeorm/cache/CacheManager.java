package com.coretex.core.activeorm.cache;

import java.util.Map;
import java.util.concurrent.Callable;

public abstract class CacheManager {
	private final CacheConfiguration cacheConfiguration;

	public CacheManager(CacheConfiguration cacheConfiguration) {
		this.cacheConfiguration = cacheConfiguration;
		init();
	}

	public abstract void init();

	public CacheConfiguration getCacheConfiguration() {
		return cacheConfiguration;
	}

	public abstract <K, V> V get(K key, Callable<? extends V> loader);

	public abstract <K, V> void put(K key, Callable<? extends V> loader);

	public abstract <K, V> V getIfPresent(K key);

	public abstract Stats<?> stats();

	public abstract <K> void invalidate(K key);

	public abstract Map<Object, Object> toMap();

	public abstract <K> void invalidate(Iterable<K> keys);

	public abstract void invalidateAll();
}

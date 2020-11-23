package com.coretex.core.activeorm.cache.impl;

import java.util.ArrayList;
import java.util.List;

public class StreamAsListCacheValueStoringStrategy<T> extends StreamAsCollectionCacheValueStoringStrategy<T, List<T>> {

	@Override
	public List<T> createCollection() {
		return new ArrayList<>();
	}
}

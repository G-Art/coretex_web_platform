package com.coretex.core.activeorm.services;

import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SearchResult<T> extends ReactiveSearchResult<T> {

	private Logger LOG = LoggerFactory.getLogger(SearchResult.class);

	private List<T> result;
	private int count;

	public SearchResult(Supplier<Flux<T>> resultSupplier) {
		super(resultSupplier);
		List<T> result = Collections.emptyList();
		try {
			result = getResultStream().collectList().block();
		} catch (Exception e) {
			LOG.error("No result by search was supplied", e);
		}
		this.result = CollectionUtils.isNotEmpty(result) ? ImmutableList.copyOf(result) : Collections.emptyList();
		this.count = this.result.size();
	}

	public List<T> getResult() {
		return result;
	}

	public int getCount() {
		return count;
	}

	@Override
	public Flux<T> getResultStream() {
		if (Objects.nonNull(this.result)) {
			return Flux.fromIterable(this.result);
		}
		return super.getResultStream();
	}
}

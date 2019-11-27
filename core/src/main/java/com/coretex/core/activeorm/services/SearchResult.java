package com.coretex.core.activeorm.services;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SearchResult<T> {

	private Logger LOG = LoggerFactory.getLogger(SearchResult.class);

	private List<T> result;
	private int count;
	private long executionTime;

	public SearchResult(Supplier<List<T>> resultSupplier) {

		List<T> result = Collections.emptyList();
		try{
			var timer = Stopwatch.createStarted();
			result = resultSupplier.get();
			timer.stop();
			executionTime = timer.elapsed(TimeUnit.MILLISECONDS);
		}catch (Exception e){
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

	public long getExecutionTime() {
		return executionTime;
	}

	public Stream<T> stream() {
		return result.stream();
	}
}

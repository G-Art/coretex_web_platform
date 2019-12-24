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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchResult<T> extends ReactiveSearchResult<T> {

	private Logger LOG = LoggerFactory.getLogger(SearchResult.class);

	private List<T> result;
	private int count;
	private long executionTime;

	public SearchResult(Supplier<Stream<T>> resultSupplier) {
		super(resultSupplier);
	}

	protected void sureDataLoaded(){
		if(CollectionUtils.isEmpty(this.result)){
			List<T> result = Collections.emptyList();
			try{
				var timer = Stopwatch.createStarted();
				result = getResultStream().collect(Collectors.toList());
				timer.stop();
				executionTime = timer.elapsed(TimeUnit.MILLISECONDS);
			}catch (Exception e){
				LOG.error("No result by search was supplied", e);
			}
			this.result = CollectionUtils.isNotEmpty(result) ? ImmutableList.copyOf(result) : Collections.emptyList();
			this.count = this.result.size();
		}
	}

	public List<T> getResult() {
		sureDataLoaded();
		return result;
	}

	public int getCount() {
		sureDataLoaded();
		return count;
	}

	public long getExecutionTime() {
		sureDataLoaded();
		return executionTime;
	}

	@Override
	public Stream<T> getResultStream() {
		if(CollectionUtils.isNotEmpty(this.result)){
			return this.result.stream();
		}
		return super.getResultStream();
	}
}

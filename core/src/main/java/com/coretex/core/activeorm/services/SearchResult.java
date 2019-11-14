package com.coretex.core.activeorm.services;

import com.coretex.core.activeorm.query.operations.SelectOperation;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SearchResult<T> {

	private Logger LOG = LoggerFactory.getLogger(SearchResult.class);

	private SelectOperation<T> selectOperation;
	private List<T> result;
	private int count;
	private long executionTime;

	public SearchResult(SelectOperation<T> selectOperation, Supplier<List<T>> resultSupplier) {
		this.selectOperation = selectOperation;

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

	public SelectOperationSpec<T> nextPage(){
		var operationSpec = selectOperation.getOperationSpec();
		if (Objects.nonNull(operationSpec.getCount()) && Objects.nonNull(operationSpec.getStart())){
			operationSpec.setStart(operationSpec.getStart()+operationSpec.getCount());
			return operationSpec;
		}else {
			throw new IllegalStateException("Query [" + operationSpec.getQuery() +"] is not pageable");
		}
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public Stream<T> stream() {
		return result.stream();
	}
}

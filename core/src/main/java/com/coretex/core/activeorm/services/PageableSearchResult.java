package com.coretex.core.activeorm.services;

import com.coretex.core.activeorm.query.operations.PageableSelectOperation;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PageableSearchResult<T> extends SearchResult<T> {

	private Logger LOG = LoggerFactory.getLogger(PageableSearchResult.class);

	private transient PageableSelectOperation<T> selectOperation;

	private Long totalCount;
	private Integer totalPages;

	public PageableSearchResult(PageableSelectOperation<T> selectOperation, Supplier<Stream<T>> resultSupplier) {
		super(resultSupplier);
		this.selectOperation = selectOperation;
		this.totalCount = selectOperation.getTotalCount();
		this.totalPages = (int) Math.ceil(((double) totalCount)
				/ selectOperation.getOperationSpec().getCount());
	}

	public SelectOperationSpec<T> nextPage(){
		var operationSpec = selectOperation.getOperationSpec();
		if (Objects.nonNull(operationSpec.getCount()) && Objects.nonNull(operationSpec.getPage())){
			operationSpec.setPage(operationSpec.getPage()+1);
			return operationSpec;
		}else {
			throw new IllegalStateException("Query [" + operationSpec.getQuery() +"] is not pageable");
		}
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public Integer getTotalPages() {
		return totalPages;
	}
}

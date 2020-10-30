package com.coretex.core.activeorm.query.specs.select;

import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.PageableSelectOperation;
import net.sf.jsqlparser.statement.select.Select;

import java.util.Map;
import java.util.Objects;

public class PageableSelectOperationSpec<R> extends SelectOperationSpec<R> {

	private Long count = 100L;
	private Long page = 0L;


	public PageableSelectOperationSpec(String query) {
		super(query);
	}

	public PageableSelectOperationSpec(String query, Map parameters) {
		super(query, parameters);
	}

	public String getTotalCountQuery(){
		return String.format("SELECT count(*) FROM (%s) as co", super.getQuery());
	}

	@Override
	public String getQuery() {
		if(Objects.nonNull(count) || Objects.nonNull(page)){
			StringBuilder queryBuilder = new StringBuilder(super.getQuery());
			if(Objects.nonNull(count)){
				queryBuilder.append(" LIMIT ").append(count).append(" ");
			}

			if(Objects.nonNull(page)){
				queryBuilder.append(" OFFSET ").append(count * page).append(" ");
			}

			return queryBuilder.toString();
		}
		return super.getQuery();
	}

	@Override
	public PageableSelectOperation<R> createOperation(QueryTransformationProcessor<QueryStatementContext<Select>> processorSupplier) {
		return new PageableSelectOperation<R>(this, processorSupplier);
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}
}

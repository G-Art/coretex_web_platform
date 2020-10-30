package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.sources.SelectSqlParameterSource;
import com.coretex.core.activeorm.query.specs.select.PageableSelectOperationSpec;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;


public class PageableSelectOperation<T> extends SelectOperation<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PageableSelectOperation.class);

	private PageableSelectOperationSpec<T> pageableSelectOperationSpec;

	private Long totalCount = 0L;

	public PageableSelectOperation(PageableSelectOperationSpec<T> operationSpec, QueryTransformationProcessor<QueryStatementContext<Select>> transformationProcessor) {
		super(operationSpec, transformationProcessor);
		this.pageableSelectOperationSpec = operationSpec;
	}

	@Override
	public void execute() {
		super.execute();
		Select countStatement = parseQuery(pageableSelectOperationSpec.getTotalCountQuery());
		var results = getJdbcTemplate().query(countStatement.toString(),
				new SelectSqlParameterSource(getOperationSpec()),
				getExtractorFunction().apply(this)).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(results)){
			Map rowResultMap = (Map) results.iterator().next();
			totalCount = (Long) rowResultMap.get("count");
		}
	}

	@Override
	public PageableSelectOperationSpec<T> getOperationSpec() {
		return pageableSelectOperationSpec;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}

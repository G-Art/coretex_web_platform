package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.cache.CacheService;
import com.coretex.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.contexts.SelectOperationConfigContext;
import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.specs.select.PageableSelectOperationSpec;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PageableSelectOperation extends SelectOperation {

	private static final Logger LOGGER = LoggerFactory.getLogger(PageableSelectOperation.class);

	private final CortexContext cortexContext;


	public PageableSelectOperation(QueryTransformationProcessor<QueryInfoHolder<Select>> transformationProcessor,
	                               CortexContext cortexContext, ResultSetExtractor<Stream<?>> extractor, CacheService cacheService) {
		super(transformationProcessor, cortexContext, extractor, cacheService);
		this.cortexContext = cortexContext;
	}

	@Override
	public <T> Stream<T> execute(SelectOperationConfigContext operationConfigContext) {
		var execute = super.<T>execute(operationConfigContext);

		searchQuerySupplier.andThen(query -> {
			var selectOperationSpec = (PageableSelectOperationSpec)operationConfigContext.getOperationSpec();
			String countQuery = getTotalCountQuery(selectOperationSpec.getOriginalQuery());
			SelectOperationSpec operationSpec = new SelectOperationSpec(countQuery, operationConfigContext.getOperationSpec().getParameters());
			var results = getOrmOperationExecutor().execute(operationSpec.createOperationContext()).getResultStream().collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(results)) {
				Map rowResultMap = (Map) results.iterator().next();
				operationConfigContext.setTotalCount((Long) rowResultMap.get("count"));
			}
			return null;
		}).apply(operationConfigContext.getOperationSpec());

		return execute;
	}

	private String getTotalCountQuery(String query) {
		return String.format("SELECT count(*) FROM (%s) as co", query);
	}

	@Lookup
	public ActiveOrmOperationExecutor getOrmOperationExecutor(){
		return null;
	}

}

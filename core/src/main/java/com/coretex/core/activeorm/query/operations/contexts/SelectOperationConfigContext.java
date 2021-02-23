package com.coretex.core.activeorm.query.operations.contexts;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import com.coretex.core.activeorm.services.SearchResult;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SelectOperationConfigContext extends AbstractOperationConfigContext<
		Select,
		SelectOperationSpec,
		SelectOperationConfigContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectOperationConfigContext.class);

	private final boolean pageable;
	private Long totalCount = 0L;

	public SelectOperationConfigContext(SelectOperationSpec operationSpec){
		this(operationSpec, false);
	}

	public SelectOperationConfigContext(SelectOperationSpec operationSpec, boolean pageable) {
		super(operationSpec);
		this.pageable = pageable;
	}

	@Override
	public Supplier<String> getQuerySupplier() {
		return () -> getOperationSpec().getQuery();
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.SELECT;
	}

	public Optional<BiFunction<Row, RowMetadata, ?>> customMapper(){
		return Optional.ofNullable(getOperationSpec().getCustomMapper());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends ReactiveSearchResult<T>, T> R wrapResult(Flux<T> result) {
		return pageable ? (R) new PageableSearchResult<T>(this, () -> result) : (R) new SearchResult<T>(() -> result);
	}

	public boolean isPageable() {
		return pageable;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}

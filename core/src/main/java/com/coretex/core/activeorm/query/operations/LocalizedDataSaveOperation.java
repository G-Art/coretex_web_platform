package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.LocalizedDataSaveOperationConfigContext;
import com.coretex.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.services.bootstrap.DbDialectService;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.statement.Statement;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.math.MathFlux;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class LocalizedDataSaveOperation extends ModificationOperation<Statement, LocalizedDataSaveOperationSpec, LocalizedDataSaveOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(LocalizedDataSaveOperation.class);
	private DbDialectService dbDialectService;

	public LocalizedDataSaveOperation(AbstractJdbcService abstractJdbcService, DbDialectService dbDialectService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService);
		this.dbDialectService = dbDialectService;
	}

	@Override
	protected Mono<Integer> executeBefore(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		//not required
		return Mono.just(0);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_SAVE;
	}

	@Override
	public Mono<Integer> executeOperation(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		LocalizedDataSaveOperationSpec.LocalizedAttributeSaveFetcher fetcher = operationSpec.getFetcher();

		return Mono.fromSupplier(() -> {
			var i = 0;
			if (fetcher.hasValuesForInsert()) {
				Map<Locale, Object> insertValues = fetcher.getInsertValues();
				var query = operationSpec.getInsertQuery();
				if (LOG.isDebugEnabled()) {
					LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
				}
				i = i + executeReactiveOperation(databaseClient ->
						MathFlux.sumInt(
								Flux.fromIterable(buildParams(insertValues, operationSpec))
										.map(stringObjectMap -> {
											var sql = bindForEach(
													databaseClient.sql(query),
													stringObjectMap,
													(spec, entry) -> {
														if (Objects.isNull(entry.getValue().getLeft())) {
															return spec.bindNull(entry.getKey(), entry.getValue().getRight());
														}
														return spec.bind(entry.getKey(), entry.getValue().getLeft());
													}
											);
											return sql.fetch().rowsUpdated();
										})
										.defaultIfEmpty(Mono.just(0))
										.map(Mono::block))).block();
			}
			if (fetcher.hasValuesForUpdate()) {
				Map<Locale, Object> updateValues = fetcher.getUpdateValues();
				var query = operationSpec.getUpdateQuery();
				if (LOG.isDebugEnabled()) {
					LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
				}
				i = i + executeReactiveOperation(databaseClient ->
						MathFlux.sumInt(
								Flux.fromIterable(buildParams(updateValues, operationSpec))
										.map(stringObjectMap -> {
											var sql = bindForEach(
													databaseClient.sql(query),
													stringObjectMap,
													(spec, entry) -> {
														if (Objects.isNull(entry.getValue().getLeft())) {
															return spec.bindNull(entry.getKey(), entry.getValue().getRight());
														}
														return spec.bind(entry.getKey(), entry.getValue().getLeft());
													}
											);
											return sql.fetch().rowsUpdated();
										})
										.defaultIfEmpty(Mono.just(0))
										.map(Mono::block))).block();
			}
			return i;
		});
	}

	private List<Map<String, Pair<Object, Class<?>>>> buildParams(Map<Locale, Object> values, LocalizedDataSaveOperationSpec operationSpec) {
		List<Map<String, Pair<Object, Class<?>>>> parameterSources = Lists.newArrayList();
		values.forEach((locale, o) -> {
			Map<String, Pair<Object, Class<?>>> params = new HashMap<>();
			params.put("owner", Pair.of(operationSpec.getItem().getUuid(), UUID.class));
			params.put("attribute", Pair.of(operationSpec.getAttributeTypeItem().getUuid(), UUID.class));
			params.put("localeiso", Pair.of(locale.toString(), String.class));
			params.put("value", Pair.of(toString(o, operationSpec.getAttributeTypeItem()), String.class));
			parameterSources.add(params);
		});
		return parameterSources;
	}

	private Object toString(Object o, MetaAttributeTypeItem attribute) {
		if (AttributeTypeUtils.isRegularTypeAttribute(attribute)) {
			Class regularClass = AttributeTypeUtils.getItemClass(attribute);
			if (regularClass != null && regularClass.isAssignableFrom(Class.class)) {
				return ((Class) o).getName();
			}
			if (regularClass != null && regularClass.equals(Date.class)) {
				return dbDialectService.dateToString(new java.sql.Timestamp(((Date) o).getTime()));
			}
		}
		return o;
	}

	@Override
	protected Mono<Integer> executeAfter(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		//not required
		return Mono.just(0);
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}

	@Override
	protected boolean useInterceptors(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		return false;
	}
}

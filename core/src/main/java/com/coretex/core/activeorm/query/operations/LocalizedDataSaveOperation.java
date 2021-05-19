package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.LocalizedDataSaveOperationConfigContext;
import com.coretex.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.services.bootstrap.DbDialectService;
import com.coretex.items.core.MetaAttributeTypeItem;
import net.sf.jsqlparser.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalizedDataSaveOperation extends ModificationOperation<Statement,LocalizedDataSaveOperationSpec, LocalizedDataSaveOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(LocalizedDataSaveOperation.class);
	private DbDialectService dbDialectService;

	public LocalizedDataSaveOperation(AbstractJdbcService abstractJdbcService, DbDialectService dbDialectService, ItemOperationInterceptorService itemOperationInterceptorService) {
		super(abstractJdbcService, itemOperationInterceptorService);
		this.dbDialectService = dbDialectService;
	}

	@Override
	protected void executeBefore(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		//not required
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_SAVE;
	}

	@Override
	public void executeOperation(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		var operationSpec = operationConfigContext.getOperationSpec();
		LocalizedDataSaveOperationSpec.LocalizedAttributeSaveFetcher fetcher = operationSpec.getFetcher();

		if(fetcher.hasValuesForInsert()){
			Map<Locale, Object> insertValues = fetcher.getInsertValues();
			var query = operationSpec.getInsertQuery();
			if(LOG.isDebugEnabled()){
				LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
			}
			executeJdbcOperation(jdbcTemplate -> jdbcTemplate.batchUpdate(query, buildParams(insertValues, operationSpec)));
		}
		if(fetcher.hasValuesForUpdate()){
			Map<Locale, Object> updateValues = fetcher.getUpdateValues();
			var query = operationSpec.getUpdateQuery();
			if(LOG.isDebugEnabled()){
				LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
			}
			executeJdbcOperation(jdbcTemplate -> jdbcTemplate.batchUpdate(query, buildParams(updateValues, operationSpec)));
		}
	}

	private SqlParameterSource[] buildParams(Map<Locale, Object> values, LocalizedDataSaveOperationSpec operationSpec) {
		SqlParameterSource[] parameterSources = new SqlParameterSource[values.size()];
		AtomicInteger index = new AtomicInteger();
		values.forEach((locale, o) -> {
			Map<String, Object> params = new HashMap<>();
			params.put("owner", operationSpec.getItem().getUuid());
			params.put("attribute", operationSpec.getAttributeTypeItem().getUuid());
			params.put("localeiso", locale.toString());
			params.put("value", toString(o, operationSpec.getAttributeTypeItem()));
			parameterSources[index.getAndIncrement()] = new MapSqlParameterSource(params);
		});
		return parameterSources;
	}

	private Object toString(Object o, MetaAttributeTypeItem attribute) {
		if(AttributeTypeUtils.isRegularTypeAttribute(attribute)){
			Class regularClass = AttributeTypeUtils.getItemClass(attribute);
			if(regularClass != null && regularClass.isAssignableFrom(Class.class)){
				return ((Class)o).getName();
			}
			if(regularClass != null && regularClass.equals(Date.class)){
				return dbDialectService.dateToString(new java.sql.Timestamp(((Date)o).getTime()));
			}
		}
		return o;
	}

	@Override
	protected void executeAfter(LocalizedDataSaveOperationConfigContext operationConfigContext) {
		//not required
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

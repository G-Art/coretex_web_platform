package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.LocalizedDataSaveOperationSpec;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.services.bootstrap.DbDialectService;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.server.ApplicationContextProvider;
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

public class LocalizedDataSaveOperation extends ModificationOperation<Statement, LocalizedDataSaveOperationSpec> {

	private Logger LOG = LoggerFactory.getLogger(LocalizedDataSaveOperation.class);
	private DbDialectService dbDialectService;

	public LocalizedDataSaveOperation(LocalizedDataSaveOperationSpec operationSpec) {
		super(operationSpec);
		dbDialectService = ApplicationContextProvider.getApplicationContext().getBean(DbDialectService.class);
	}

	@Override
	protected void executeBefore() {
		//not required
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LOCALIZED_DATA_SAVE;
	}

	@Override
	public void executeOperation() {
		LocalizedDataSaveOperationSpec.LocalizedAttributeSaveFetcher fetcher = getOperationSpec().getFetcher();

		if(fetcher.hasValuesForInsert()){
			Map<Locale, Object> insertValues = fetcher.getInsertValues();
			var query = getOperationSpec().getInsertQuery();
			if(LOG.isDebugEnabled()){
				LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
			}
			executeJdbcOperation(jdbcTemplate -> jdbcTemplate.batchUpdate(query, buildParams(insertValues)));
		}
		if(fetcher.hasValuesForUpdate()){
			Map<Locale, Object> updateValues = fetcher.getUpdateValues();
			var query = getOperationSpec().getUpdateQuery();
			if(LOG.isDebugEnabled()){
				LOG.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
			}
			executeJdbcOperation(jdbcTemplate -> jdbcTemplate.batchUpdate(query, buildParams(updateValues)));
		}
	}

	private SqlParameterSource[] buildParams(Map<Locale, Object> values) {
		SqlParameterSource[] parameterSources = new SqlParameterSource[values.size()];
		AtomicInteger index = new AtomicInteger();
		values.forEach((locale, o) -> {
			Map<String, Object> params = new HashMap<>();
			params.put("owner", getOperationSpec().getItem().getUuid());
			params.put("attribute", getOperationSpec().getAttributeTypeItem().getUuid());
			params.put("localeiso", locale.toString());
			params.put("value", toString(o, getOperationSpec().getAttributeTypeItem()));
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
	protected void executeAfter() {
		//not required
	}

	@Override
	protected boolean isTransactionInitiator() {
		return false;
	}

}

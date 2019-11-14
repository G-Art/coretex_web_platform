package com.coretex.core.activeorm.query;

import com.coretex.items.core.MetaTypeItem;
import net.sf.jsqlparser.statement.Statement;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueryContext <T extends Statement> {

	private String rawQuery;
	private QueryType queryType;
	private T statement;
	private MetaTypeItem targetType;
	private Map<String, Object> parameters;

	private String preparedQuery;

	public QueryContext(String rawQuery) {
		this.rawQuery = rawQuery;
		this.parameters = new ConcurrentHashMap<>();
	}

	public String getRawQuery() {
		return rawQuery;
	}

	public QueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	public T getStatement() {
		return statement;
	}

	public void setStatement(T statement) {
		this.statement = statement;
	}

	public String getPreparedQuery() {
		return preparedQuery;
	}

	public void setPreparedQuery(String preparedQuery) {
		this.preparedQuery = preparedQuery;
	}

	public MetaTypeItem getTargetType() {
		return targetType;
	}

	public void setTargetType(MetaTypeItem targetType) {
		this.targetType = targetType;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public QueryContext setParameter(String name, Object val) {
		parameters.put(name, val);
		return this;
	}

	public QueryContext setParameters(Map<String, ?> parameters) {
		if (MapUtils.isNotEmpty(parameters)) {
			this.parameters.putAll(parameters);
		}
		return this;
	}
}

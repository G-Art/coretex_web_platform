package com.coretex.core.activeorm.cache.impl;

import com.coretex.core.activeorm.cache.CacheContext;
import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.items.core.MetaTypeItem;
import net.sf.jsqlparser.statement.Statement;

import java.util.Map;
import java.util.Set;

public class FeaturedStatementCacheContext<S extends Statement> implements CacheContext {

	private final QueryInfoHolder<S> queryInfoHolder;
	private Map<String, Object> parameters;

	public FeaturedStatementCacheContext(QueryInfoHolder<S> queryInfoHolder, Map<String, Object> parameters) {
		this.queryInfoHolder = queryInfoHolder;
		this.parameters = parameters;
	}

	public S getStatement() {
		return queryInfoHolder.getStatement();
	}

	public boolean isLocalized() {
		return queryInfoHolder.isLocalizedTable();
	}

	public Set<MetaTypeItem> getItemsUsed() {
		return queryInfoHolder.getItemsUsed();
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}
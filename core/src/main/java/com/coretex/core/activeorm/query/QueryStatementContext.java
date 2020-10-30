package com.coretex.core.activeorm.query;

import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Sets;
import net.sf.jsqlparser.statement.Statement;

import java.util.Set;

public class QueryStatementContext <S extends Statement> {

	private final S statement;
	private final Set<Class<MetaTypeItem>> itemsUsed = Sets.newHashSet();

	public QueryStatementContext(S statement) {
		this.statement = statement;
	}

	public S getStatement() {
		return statement;
	}

	public void addItemUsed(Class<MetaTypeItem> itemClass){
		itemsUsed.add(itemClass);
	}

	public Set<Class<MetaTypeItem>> getItemsUsed() {
		return Set.copyOf(itemsUsed);
	}
}
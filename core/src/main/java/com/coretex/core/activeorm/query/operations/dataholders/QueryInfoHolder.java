package com.coretex.core.activeorm.query.operations.dataholders;

import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Sets;
import net.sf.jsqlparser.statement.Statement;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;

public class QueryInfoHolder<S extends Statement> {
	private final S statement;
	private final Set<MetaTypeItem> itemsUsed = Sets.newHashSet();

	public QueryInfoHolder(S statement) {
		this.statement = statement;
	}

	public S getStatement() {
		return statement;
	}

	public void addItemUsed(MetaTypeItem itemClass){
		itemsUsed.add(itemClass);
	}

	public Set<MetaTypeItem> getItemsUsed() {
		return Set.copyOf(itemsUsed);
	}

	public void addAllItemUsed(Set<MetaTypeItem> metaTypes) {
		if (CollectionUtils.isNotEmpty(metaTypes)) {
			itemsUsed.addAll(metaTypes);
		}
	}

}

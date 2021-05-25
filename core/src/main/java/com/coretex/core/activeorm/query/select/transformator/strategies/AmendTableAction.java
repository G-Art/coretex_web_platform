package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.items.core.MetaTypeItem;
import net.sf.jsqlparser.statement.select.PlainSelect;

public interface AmendTableAction {
	PlainSelect amendTable(PlainSelect plainSelect, MetaTypeItem metaTypeItem);
}

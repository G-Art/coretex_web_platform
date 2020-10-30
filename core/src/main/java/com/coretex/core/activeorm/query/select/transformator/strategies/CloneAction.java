package com.coretex.core.activeorm.query.select.transformator.strategies;

import net.sf.jsqlparser.statement.select.PlainSelect;

public interface CloneAction {
	PlainSelect clone(PlainSelect select);
}

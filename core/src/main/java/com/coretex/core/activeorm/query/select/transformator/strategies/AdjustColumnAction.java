package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;

public interface AdjustColumnAction {

	void adjustColumn(ExpressionScanner scanner, SelectBodyScanner ownerSelectBodyScanner);
}

package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.select.data.TableTransformationData;
import com.coretex.core.activeorm.query.select.scanners.FromItemScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.Optional;

public class TableDataInjectionPoint extends AbstractScannerDataInjectionPoint<FromItemScanner> {

	private static final String TABLE_TRANSFORMATION_DATA = "TABLE_TRANSFORMATION_DATA";
	private static final String USE_SUBTYPES = "USE_SUBTYPES";

	public TableDataInjectionPoint(FromItemScanner scanner, QueryStatementContext<? extends Statement> context) {
		super(scanner, context);
	}

	public void setUseSubtypes(Boolean useSubtypes){
		this.addParam(USE_SUBTYPES, useSubtypes);
	}

	public boolean isUseSubtypes(){
		return this.getParam(USE_SUBTYPES);
	}

	public void setTableTransformationData(TableTransformationData tableTransformationData){
		this.addParam(TABLE_TRANSFORMATION_DATA, tableTransformationData);
	}

	public TableTransformationData getTableTransformationData(){
		return this.getParam(TABLE_TRANSFORMATION_DATA);
	}

	public Optional<SelectBodyScanner> getOwnerPlainSelectScanner(){
		return this.findParentByType(PlainSelect.class);
	}
}

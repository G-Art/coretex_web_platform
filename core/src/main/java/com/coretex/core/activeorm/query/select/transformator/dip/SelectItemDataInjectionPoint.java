package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectItemScanner;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllTableColumns;

import java.util.Objects;
import java.util.Optional;

public class SelectItemDataInjectionPoint extends AbstractScannerDataInjectionPoint<SelectItemScanner<?>> {

	private Table table;
	public SelectItemDataInjectionPoint(SelectItemScanner scanner, QueryInfoHolder<? extends Statement> context) {
		super(scanner, context);
		var selectItem = scanner.scannedObject();
		if(selectItem instanceof AllTableColumns){
			table = ((AllTableColumns) selectItem).getTable();
		}
	}

	public boolean isTableAware(){
		return Objects.nonNull(table);
	}

	public Table getTable() {
		return table;
	}

	public void setSelectBodyScannerOwner(SelectBodyScanner selectBodyScannerOwner){
		this.addParam(SELECT_BODY_OWNER, selectBodyScannerOwner);
	}

	@Override
	public Optional<SelectBodyScanner> getSelectBodyScannerOwner(){
		return super.getSelectBodyScannerOwner();
	}

}

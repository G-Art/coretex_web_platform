package com.coretex.core.activeorm.query.select.scanners;


import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.Objects;

import static com.coretex.core.activeorm.query.select.transformator.DataInjectionType.SELECT_ITEM_EXPRESSION;


public class SelectItemScanner<Q> extends Scanner<SelectItem, Q> {

	private SelectItem selectItem;
	private boolean isAllColumn = false;
	private ExpressionScanner<SelectItem> expressionScanner;
	private int index;

	public SelectItemScanner(int deep, Q parentStatement, int index) {
		super(deep, parentStatement);
		this.index = index;
	}

	public SelectItemScanner(int deep, Q parentStatement, Scanner parent, int index) {
		super(deep, parentStatement, parent);
		this.index = index;
	}

	@Override
	public void scan(SelectItem query) {
		query.accept(this);
	}

	@Override
	public SelectItem scannedObject() {
		return selectItem;
	}

	@Override
	public Class<? extends SelectItem> scannedObjectClass() {
		return selectItem.getClass();
	}

	@Override
	public void visit(AllColumns tableColumn) {
		isAllColumn = true;
		this.selectItem = tableColumn;
		adjustUUID();
	}

	@Override
	public void visit(SelectExpressionItem item) {
		this.selectItem = item;
		expressionScanner = new ExpressionScanner<>(getDeep()+1, item, this, SELECT_ITEM_EXPRESSION);
		expressionScanner.scan(item.getExpression());
	}

	@Override
	public void visit(AllTableColumns tableColumn) {
		isAllColumn = true;
		this.selectItem = tableColumn;
		adjustUUID();
	}

	private void adjustUUID(){
		Scanner prnt = getParent();
		while (Objects.nonNull(prnt)){
			if(prnt instanceof SelectBodyScanner){
				((SelectBodyScanner) prnt).setUUIDColumnPresent(true);
				prnt=null;
			}else{
				prnt = prnt.getParent();
			}
		}
	}

	public boolean isAllColumn() {
		return isAllColumn;
	}

	public int getIndex() {
		return index;
	}

	public ExpressionScanner<SelectItem> getExpressionScanner() {
		return expressionScanner;
	}
}

package com.coretex.core.activeorm.query.select.scanners;


import com.coretex.core.activeorm.query.select.data.AliasInfoHolder;
import com.coretex.core.activeorm.query.select.data.TableTransformationData;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.Objects;


public class FromItemScanner<Q> extends Scanner<FromItem, Q> {

	private FromItem fromItem;
	private boolean isTable = false;
	private boolean useInheritance = true;
	private TableTransformationData tableTransformationData;

	public FromItemScanner(int deep, Q parentStatement) {
		super(deep, parentStatement);
	}

	public FromItemScanner(int deep, Q parentStatement, Scanner parent) {
		super(deep, parentStatement, parent);
	}

	@Override
	public void scan(FromItem query) {
		this.fromItem = query;
		query.accept(this);
	}

	@Override
	public void visit(Table table) {
		Scanner prnt = getParent();
		while (Objects.nonNull(prnt)){
			if(prnt instanceof SelectBodyScanner){
				if(table.getName().replaceAll("\"", "").startsWith("#")){
					useInheritance = false;
					table.setName(table.getName().replace("#", ""));
				}
				((SelectBodyScanner) prnt).addAliasInfoHolder(table.getName(), new AliasInfoHolder<>(table.getName(), this));
				if(Objects.nonNull(table.getAlias())){
					((SelectBodyScanner) prnt).addAliasInfoHolder(table.getAlias().getName(), new AliasInfoHolder<>(table.getAlias().getName(), this));
				}
				prnt=null;
			}else{
				prnt = prnt.getParent();
			}
		}

		tableTransformationData = transformationHelper.bindItem(table);

		isTable = true;
	}

	@Override
	public FromItem scannedObject() {
		return fromItem;
	}

	public TableTransformationData getTableTransformationData() {
		return tableTransformationData;
	}

	@Override
	public Class<? extends FromItem> scannedObjectClass() {
		return fromItem.getClass();
	}

	public boolean isTable() {
		return isTable;
	}

	public boolean isUseInheritance() {
		return useInheritance;
	}
}

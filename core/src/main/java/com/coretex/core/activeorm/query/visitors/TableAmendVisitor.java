package com.coretex.core.activeorm.query.visitors;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

public class TableAmendVisitor extends AbstranctAmendmentVisitor {
	private String oldName;
	private String newName;

	public TableAmendVisitor(String oldName, String newName) {
		this.oldName = oldName;
		this.newName = newName;
	}

	public void visit(Table tableName) {
		super.visit(tableName);
		if (tableName.getName().equals(oldName)) {
			tableName.setName(newName);
		}
	}

	public void visit(Column tableColumn) {
		super.visit(tableColumn);
		if(tableColumn.getTable() == null){
			tableColumn.setTable(new Table(newName));
		}

	}

}

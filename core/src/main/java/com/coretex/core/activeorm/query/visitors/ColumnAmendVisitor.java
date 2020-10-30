package com.coretex.core.activeorm.query.visitors;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.Objects;

public class ColumnAmendVisitor extends AbstranctAmendmentVisitor {
	private String oldName;
	private String newName;
	private final Table oldTable;
	private final Table newTable;

	public ColumnAmendVisitor(String oldName, String newName, Table oldTable, Table newTable) {
		this.oldName = oldName;
		this.newName = newName;
		this.oldTable = oldTable;
		this.newTable = newTable;
	}

	public void visit(Column tableColumn) {
		super.visit(tableColumn);

		if(tableColumn.getTable() == null){
			tableColumn.setTable(newTable);
			tableColumn.setColumnName(newName);

		}else{
			var oldTable = tableColumn.getTable();
			if(tableColumn.getColumnName().equals(oldName)){
				if(Objects.equals(oldTable.getName(), this.oldTable.getName()) || isSameAlias(oldTable.getAlias(), this.oldTable.getAlias())){
					tableColumn.setTable(newTable);
					tableColumn.setColumnName(newName);
				}
			}
		}
	}

	private boolean isSameAlias(Alias alias, Alias alias1) {
		return (alias == alias1) || (alias != null && alias1 != null && Objects.equals(alias.getName(), alias1.getName()));
	}

}

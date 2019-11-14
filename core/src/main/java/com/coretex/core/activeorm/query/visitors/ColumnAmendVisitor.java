package com.coretex.core.activeorm.query.visitors;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.comment.Comment;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import net.sf.jsqlparser.statement.values.ValuesStatement;

import java.util.Iterator;
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

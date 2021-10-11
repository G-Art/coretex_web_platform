package com.coretex.core.services.bootstrap.dialect;

import com.coretex.core.activeorm.query.select.data.TableTransformationData;
import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.server.ApplicationContextProvider;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface QueryTransformationHints {

	default Expression createColumn(TableTransformationData tableTransformationData, MetaAttributeTypeItem attribute){
		return new Column(tableTransformationData.getTable(), attribute.getColumnName());
	}

	default SelectExpressionItem createSelectExpressionItem(TableTransformationData tableTransformationData, MetaAttributeTypeItem attribute){
		SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
		selectExpressionItem.setExpression(createColumn(tableTransformationData, attribute));
		return selectExpressionItem;
	}

	default Expression createMetaEqExpression(MetaTypeItem meta, Expression left, Set<MetaTypeItem> subTypeItemSet){
			Expression whereExpression;
			if (CollectionUtils.isNotEmpty(subTypeItemSet)) {

				whereExpression = new InExpression();
				((InExpression) whereExpression).setLeftExpression(left);
				ExpressionList expressionList = new ExpressionList();
				List<Expression> expressions = new ArrayList<>();
				expressions.add(new StringValue(meta.getUuid().toString()));

				subTypeItemSet.forEach(sub -> expressions.add(new StringValue(sub.getUuid().toString())));
				expressionList.setExpressions(expressions);
				((InExpression) whereExpression).setRightItemsList(expressionList);

			} else {
				whereExpression = new EqualsTo();
				((EqualsTo) whereExpression).setLeftExpression(left);
				Expression right = new StringValue(meta.getUuid().toString());
				((EqualsTo) whereExpression).setRightExpression(right);

			}
			return whereExpression;
	}

	default void adjustParameter(ExpressionScanner expressionScanner, SelectBodyScanner selectBodyScanner){
		//no action by default
	}

	default void adjustColumn(ExpressionScanner scanner, TableTransformationData tableTransformationData, SelectBodyScanner ownerSelectBodyScanner){
		Column column = (Column) scanner.getExpression();
		if (Objects.nonNull(tableTransformationData)) {
			if (Objects.isNull(column.getTable()) && !ownerSelectBodyScanner.isWrapped()) {
				column.setTable(tableTransformationData.getTable());
			}
			if (Objects.isNull(column.getTable()) && ownerSelectBodyScanner.isWrapped()) {
				var table = new Table();
				table.setAlias(((PlainSelect) ownerSelectBodyScanner.scannedObject()).getFromItem().getAlias());
				column.setTable(table);
			}

			if (tableTransformationData.isBind()) {
				MetaAttributeTypeItem metaAttributeTypeItem = ApplicationContextProvider.getCoretexContext()
						.findAttribute(
								tableTransformationData.getTypeItemBind().getTypeCode(),
								column.getColumnName().replaceAll("\"", "")
						);
				if (Objects.nonNull(metaAttributeTypeItem)) {
					column.setColumnName(metaAttributeTypeItem.getColumnName());
				}
			}
		}
	}
}

package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.data.TableTransformationData;
import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.JoinScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.JoinDataInjectionPoint;
import com.coretex.core.general.utils.ItemUtils;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;

public class JoinTransformationStrategy extends AbstractTransformationStrategy<JoinDataInjectionPoint, Join> {

	private Logger LOG = LoggerFactory.getLogger(JoinTransformationStrategy.class);

	public JoinTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		super(transformationStrategies, transformationHelper);
	}

	@Override
	public Join apply(JoinDataInjectionPoint dataInjectionPoint) {
		var scanner = dataInjectionPoint.getScanner();
		var fromItemScanner = scanner.getFromItemScanner();
		if (fromItemScanner.isTable()) {
			Table table = (Table) fromItemScanner.scannedObject();
			var tableTransformationData = getTransformationHelper().bindItem(table);
			if (dataInjectionPoint.getSelectBodyScannerOwner().isPresent()) {
				adjustColumn(scanner.getJoinOnExpressionScanner(), dataInjectionPoint.getSelectBodyScannerOwner().get());
			}
			if (tableTransformationData.hasInheritance()) {
				modifyJoinInheritance(scanner, tableTransformationData);
			} else {
				modifyJoin(scanner, tableTransformationData);
			}
		}
		return null;
	}

	private void modifyJoin(JoinScanner scanner, TableTransformationData tableTransformationData) {
		Join join = scanner.scannedObject();
		if(tableTransformationData.isBind()){
			Table table = (Table) join.getRightItem();
			table.setName(tableTransformationData.getTypeItemBind().getTableName());
		}
		join.setOnExpression(adjustExpression(join.getOnExpression(), tableTransformationData.getTable(), tableTransformationData.getTypeItemBind()));
	}

	private void modifyJoinInheritance(JoinScanner scanner, TableTransformationData tableTransformationData) {
		Join join = scanner.scannedObject();

		SubSelect joinSubSelect = new SubSelect();
		SetOperationList setOperationList = new SetOperationList();
		List<SelectBody> bodies = Lists.newArrayList();
		List<Boolean> brackets = Lists.newArrayList(FALSE);
		List<SetOperation> setOperations = Lists.newArrayList();
		tableTransformationData.getInheritance().orElseGet(Collections::emptySet)
				.stream().filter(metaTypeItem -> !metaTypeItem.getTableName().equals(tableTransformationData.getTypeItemBind().getTableName()))
				.forEach(metaTypeItem -> {
					if (LOG.isDebugEnabled()) {
						LOG.debug(String.format("Add union for subtype [%s]", metaTypeItem.getTypeCode()));
					}
					bodies.add(createPlaneSelect(tableTransformationData.getTypeItemBind(), metaTypeItem, scanner.getJoinOnExpressionScanner(), createConsumer(tableTransformationData)));
					brackets.add(FALSE);
					UnionOp union = new UnionOp();
					union.setAll(true);
					setOperations.add(union);
				});
		bodies.add(createPlaneSelect(tableTransformationData.getTypeItemBind(), scanner.getJoinOnExpressionScanner(), createConsumer(tableTransformationData)));

		setOperationList.setBracketsOpsAndSelects(brackets, bodies, setOperations);

		joinSubSelect.setAlias(join.getRightItem().getAlias());
		joinSubSelect.setSelectBody(setOperationList);
		join.setRightItem(joinSubSelect);
	}

	private Consumer<PlainSelect> createConsumer(TableTransformationData tableTransformationData){
		return plainSelect -> {
			ReplaceColumnTable replaceColumnTable = new ReplaceColumnTable(tableTransformationData.getTable(), (Table) plainSelect.getFromItem());
			plainSelect.getWhere().accept(replaceColumnTable);
		};
	}

	private SelectBody createPlaneSelect(MetaTypeItem typeItemBind, ExpressionScanner joinOnExpressionScanner, Consumer<PlainSelect> plainSelectConsumer) {
		return createPlaneSelect(typeItemBind, typeItemBind, joinOnExpressionScanner, plainSelectConsumer);
	}

	private SelectBody createPlaneSelect(MetaTypeItem typeItemBind, MetaTypeItem targetType, ExpressionScanner joinOnExpressionScanner, Consumer<PlainSelect> plainSelectConsumer) {
		Table table = new Table(targetType.getTableName());

		PlainSelect plainSelect = new PlainSelect();
		plainSelect.setFromItem(table);
		plainSelect.setWhere(createMetaTypeExpression(table, targetType));
		plainSelect.setSelectItems(genSelectItems(typeItemBind, table));

		plainSelect = clone(plainSelect); // clone to detach where expression (not optimal: try to find another way)

		plainSelectConsumer.accept(plainSelect);

		return plainSelect;
	}

	private List<SelectItem> genSelectItems(MetaTypeItem typeItemBind, Table table) {
		List<SelectItem> selectItems = Lists.newArrayList();
		getTransformationHelper().getCortexContext()
				.getAllAttributes(typeItemBind)
				.values()
				.stream()
				.filter(metaAttributeTypeItem -> Objects.nonNull(metaAttributeTypeItem.getColumnName()) )
				.forEach(metaAttributeTypeItem -> selectItems.add(new SelectExpressionItem(new Column(table, metaAttributeTypeItem.getColumnName()))));
		return selectItems;
	}

	private Expression adjustExpression(Expression expression, Table table, MetaTypeItem metaTypeItem) {
		if(Objects.isNull(metaTypeItem)){
			return expression;
		}
		if (Objects.nonNull(expression)) {
			return new AndExpression(expression, createMetaTypeExpression(table, metaTypeItem));
		} else {
			return createMetaTypeExpression(table, metaTypeItem);
		}
	}


	private Expression createMetaTypeExpression(Table table, MetaTypeItem metaTypeItem) {
		Expression left = new Column(table, getTransformationHelper()
				.getCortexContext().findAttribute(metaTypeItem.getTypeCode(), GenericItem.META_TYPE).getColumnName());

		Set<MetaTypeItem> subTypeItemSet = CollectionUtils.isNotEmpty(metaTypeItem.getSubtypes()) ? ItemUtils.getAllSubtypes(metaTypeItem).stream()
				.filter(sub -> sub.getTableName().equals(metaTypeItem.getTableName()))
				.collect(Collectors.toSet()) : null;

		return getTransformationHelper().createMetaEqExpression(metaTypeItem, left, subTypeItemSet);
	}

	private class ReplaceColumnTable extends ExpressionDeParser {

		private Table source;
		private Table target;

		public ReplaceColumnTable(Table source, Table target) {
			this.source = source;
			this.target = target;
		}

		@Override
		public void visit(Column column) {
			var table = column.getTable();
			var name = Objects.isNull(table.getAlias()) ? table.getName() : table.getAlias().getName();
			var sourceName = Objects.isNull(source.getAlias()) ? source.getName() : source.getAlias().getName();
			if (name.equals(sourceName)) {
				column.setTable(target);
			}
		}

	}

}

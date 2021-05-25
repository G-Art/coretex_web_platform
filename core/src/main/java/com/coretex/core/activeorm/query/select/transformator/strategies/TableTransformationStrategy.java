package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.exceptions.QueryException;
import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.data.TableTransformationData;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectItemScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.SelectItemDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.TableDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.WrapperInjectionPoint;
import com.coretex.core.general.utils.ItemUtils;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.Offset;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperation;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.UnionOp;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;

public class TableTransformationStrategy extends AbstractTransformationStrategy<TableDataInjectionPoint, SelectBody> {

	private Logger LOG = LoggerFactory.getLogger(TableTransformationStrategy.class);

	public TableTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		super(transformationStrategies, transformationHelper);
	}

	public SelectBody apply(TableDataInjectionPoint dataInjectionPoint) {
		var tableTransformationData = dataInjectionPoint.getTableTransformationData();
		var plainSelectScanner = dataInjectionPoint.getOwnerPlainSelectScanner();
		QueryInfoHolder<? extends Statement> context = dataInjectionPoint.getContext();

		PlainSelect originalPlainSelect = plainSelectScanner
				.map(selectBodyScanner -> (PlainSelect) selectBodyScanner.scannedObject())
				.orElseThrow(() -> {
					throw new QueryException(String.format("Plane select token is not available for [%s] table", tableTransformationData.getTable().toString()));
				});
		var selectBodyScanner = plainSelectScanner.get();

		if (addWrapper(selectBodyScanner, tableTransformationData, dataInjectionPoint.isUseSubtypes())) {
			PlainSelect wrapper = clone(originalPlainSelect);
			selectBodyScanner.setWrapped(true);
			var orderByElements = originalPlainSelect.getOrderByElements();
			Limit limit = originalPlainSelect.getLimit();
			Offset offset = originalPlainSelect.getOffset();
			originalPlainSelect.setLimit(null);
			originalPlainSelect.setOffset(null);
			originalPlainSelect.setOrderByElements(null);
			SubSelect subSelect = new SubSelect();
			if (selectBodyScanner.getFromItemScanner().isTable()) {
				Table table = (Table) selectBodyScanner.getFromItemScanner().scannedObject();
				subSelect.setAlias(table.getAlias() != null ? table.getAlias() : createAlias(table.getName(), selectBodyScanner.getDeep()));
			} else {
				var alias = selectBodyScanner.getFromItemScanner().scannedObject().getAlias();
				if (Objects.isNull(alias)) {
					subSelect.setAlias(createAlias("wrap_subs", selectBodyScanner.getDeep()));
				} else {
					subSelect.setAlias(alias);
				}
			}
			subSelect.setSelectBody(adjustInheritance(tableTransformationData, selectBodyScanner, dataInjectionPoint.isUseSubtypes(), context));
			adjustWrapperSelectItems(selectBodyScanner, wrapper);
			wrapper.setFromItem(subSelect);
			wrapper.setLimit(limit);
			wrapper.setOffset(offset);
			wrapper.setOrderByElements(orderByElements);
			wrapper.setWhere(null);
			return wrapper;
		} else {
			return adjustInheritance(tableTransformationData, selectBodyScanner, dataInjectionPoint.isUseSubtypes(), context);
		}
	}

	private void adjustWrapperSelectItems(SelectBodyScanner selectBodyScanner, PlainSelect wrapper) {
		var wrapperSelectBodyScanner = new SelectBodyScanner<>(selectBodyScanner.getDeep(), selectBodyScanner.getParentStatement(), selectBodyScanner);
		wrapperSelectBodyScanner.scan(wrapper);
		wrapperSelectBodyScanner.setWrapped(true);
		wrapperSelectBodyScanner.getSelectItemScanners().forEach(selectItemScanner -> {
			if (selectItemScanner.isAllColumn() ) {
				if(Objects.nonNull(wrapper.getFromItem().getAlias())){
					var selectItem = selectItemScanner.scannedObject();
					if (selectItem instanceof AllTableColumns) {
						var table = new Table();
						table.setAlias(new Alias(wrapper.getFromItem().getAlias().getName(), false));
						((AllTableColumns) selectItem).setTable(table);
					}
				}
			} else {
				var expressionScanner = selectItemScanner.getExpressionScanner();
				adjustColumn(expressionScanner, wrapperSelectBodyScanner);
			}

		});
	}

	private boolean addWrapper(SelectBodyScanner selectBodyScanner, TableTransformationData tableTransformationData, boolean useInheritance) {
		var selectItemScanners = (List<SelectItemScanner>) selectBodyScanner.getSelectItemScanners();
		PlainSelect plainSelect = (PlainSelect) selectBodyScanner.scannedObject();
		boolean hasOrderBy = CollectionUtils.isNotEmpty(plainSelect.getOrderByElements());
		boolean hasLimit = Objects.nonNull(plainSelect.getLimit());
		boolean hasOffset = Objects.nonNull(plainSelect.getOffset());
		return (selectItemScanners.stream().anyMatch(selectItemScanner ->
				!selectItemScanner.isAllColumn()) &&
				tableTransformationData.hasInheritance()) ||
				(tableTransformationData.hasInheritance() &&
						tableTransformationData.isUnionTable() &&
						useInheritance &&
						(hasOffset || hasLimit || hasOrderBy));
	}

	private SelectBody adjustInheritance(TableTransformationData tableTransformationData, SelectBodyScanner originalPlainSelectScanner, boolean useSubtypes, QueryInfoHolder<? extends Statement> context) {
		PlainSelect originalPlainSelect = (PlainSelect) originalPlainSelectScanner.scannedObject();
		adjustSelectItem(originalPlainSelectScanner, context);
		if (tableTransformationData.hasInheritance() && useSubtypes) {
			SetOperationList setOperationList = new SetOperationList();
			List<SelectBody> bodies = Lists.newArrayList();
			List<Boolean> brackets = Lists.newArrayList(FALSE);
			List<SetOperation> setOperations = Lists.newArrayList();
			tableTransformationData.getInheritance()
					.orElseGet(Collections::emptySet)
					.stream()
					.filter(metaTypeItem -> !metaTypeItem.getTableName().equals(tableTransformationData.getTypeItemBind().getTableName()))
					.forEach(metaTypeItem -> {
						if (LOG.isDebugEnabled()) {
							LOG.debug(String.format("Add union for subtype [%s]", metaTypeItem.getTypeCode()));
						}
						bodies.add(adjustPlaneSelect(clone(originalPlainSelect), metaTypeItem, useSubtypes));
						brackets.add(FALSE);
						UnionOp union = new UnionOp();
						union.setAll(true);
						setOperations.add(union);
					});
			bodies.add(adjustPlaneSelect(originalPlainSelect, tableTransformationData.getTypeItemBind(), useSubtypes));

			setOperationList.setBracketsOpsAndSelects(brackets, bodies, setOperations);
			return setOperationList;
		} else {
			return adjustPlaneSelect(originalPlainSelect, tableTransformationData.getTypeItemBind(), useSubtypes);
		}
	}

	private PlainSelect adjustPlaneSelect(PlainSelect plainSelect, MetaTypeItem metaTypeItem, boolean useSubtypes) {
		adjustWhere(plainSelect, metaTypeItem, useSubtypes);
		return amendTable(plainSelect, metaTypeItem);
	}

	private void adjustWhere(PlainSelect newPlainSelect, MetaTypeItem metaTypeItem, boolean useSubtypes) {
		if (Objects.nonNull(newPlainSelect.getWhere())) {
			AndExpression andExpression = new AndExpression(newPlainSelect.getWhere(), createMetaTypeWhereExpression(newPlainSelect, metaTypeItem, useSubtypes));
			newPlainSelect.setWhere(andExpression);
		} else {
			newPlainSelect.setWhere(createMetaTypeWhereExpression(newPlainSelect, metaTypeItem, useSubtypes));
		}
	}

	private Alias createAlias(String prefix, int deep) {
		return new Alias("alias_" + prefix + "_" + deep);
	}

	private Expression createMetaTypeWhereExpression(PlainSelect newPlainSelect, MetaTypeItem metaTypeItem, boolean useSubtypes) {
		Expression left = new Column((Table) newPlainSelect.getFromItem(), getTransformationHelper()
				.getCortexContext().findAttribute(metaTypeItem.getTypeCode(), GenericItem.META_TYPE).getColumnName());
		Set<MetaTypeItem> subTypeItemSet = null;
		if (useSubtypes) {
			subTypeItemSet = CollectionUtils.isNotEmpty(metaTypeItem.getSubtypes()) ?
					ItemUtils.getAllSubtypes(metaTypeItem).stream()
							.filter(sub -> sub.getTableName().equals(metaTypeItem.getTableName()))
							.collect(Collectors.toSet()) :
					null;
		}

		return getTransformationHelper().createMetaEqExpression(metaTypeItem, left, subTypeItemSet);
	}

	private void adjustSelectItem(SelectBodyScanner selectBodyScanner, QueryInfoHolder<? extends Statement> statementContext) {
		List<SelectItemScanner<?>> selectItemScanners = selectBodyScanner.getSelectItemScanners();
		selectItemScanners.stream()
				.filter(SelectItemScanner::isAllColumn)
				.forEach(selectItemScanner -> {
					SelectItemDataInjectionPoint selectItemDataInjectionPoint = getTransformationHelper().createDateInjectionPoint(selectItemScanner, statementContext);
					selectItemDataInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
					applyTransformation(selectItemDataInjectionPoint);
				});
		var scanners = selectItemScanners.stream()
				.filter(selectItemScanner -> !selectItemScanner.isAllColumn())
				.collect(Collectors.toList());

		if (CollectionUtils.isNotEmpty(scanners)) {
			WrapperInjectionPoint<List<SelectItemDataInjectionPoint>> wrapperInjectionPoint = getTransformationHelper()
					.createDateInjectionPointsWrapper(scanners, dateInjectionPoint -> {
						dateInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
						return dateInjectionPoint;
					}, statementContext);
			wrapperInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
			selectBodyScanner.setSelectBody(applyTransformation(wrapperInjectionPoint));
		}
	}
}

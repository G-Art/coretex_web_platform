package com.coretex.core.activeorm.query.select;

import com.coretex.core.activeorm.exceptions.QueryException;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.select.data.TableTransformationData;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.SelectBodyDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.strategies.AbstractTransformationStrategy;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.FullTextSearch;
import net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression;
import net.sf.jsqlparser.expression.operators.relational.SimilarToExpression;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.DeclareStatement;
import net.sf.jsqlparser.statement.ShowColumnsStatement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;


public class SelectQueryTransformationProcessor implements QueryTransformationProcessor<Select>, SelectQueryVisitors {

	private Logger LOG = LoggerFactory.getLogger(SelectQueryTransformationProcessor.class);

	private CortexContext cortexContext;

	private SelectQueryTransformationHelper helper;

	private Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies;


	public SelectQueryTransformationProcessor(CortexContext cortexContext, Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper selectQueryTransformationHelper) {
		this.cortexContext = cortexContext;
		this.transformationStrategies = transformationStrategies;
		this.helper = selectQueryTransformationHelper;
	}

	@Override
	public void transform(Select statement) {

		SelectBodyScanner<Select> selectBodyScanner = new SelectBodyScanner<>(0, statement);

		selectBodyScanner.scan(statement.getSelectBody());

		SelectBodyDataInjectionPoint selectBodyDataInjectionPoint = helper.createDateInjectionPoint(selectBodyScanner);
		SelectBody result = (SelectBody) transformationStrategies.get(selectBodyDataInjectionPoint.getDataInjectionType())
				.apply(selectBodyDataInjectionPoint);
		statement.setSelectBody(result);

//		statement.accept(this);
	}

	@Override
	public void visit(Select select) {
		if (select.getWithItemsList() != null) {
			throw new QueryException(NOT_SUPPORTED_YET + " With items query expressions.");
		}
		if (select.getSelectBody() instanceof WithItem) {
			throw new QueryException(NOT_SUPPORTED_YET + " With items query expressions.");
		}

		SelectBody originSelectBody = select.getSelectBody();
		originSelectBody.accept(new Transformation(0, select));

	}

	@Override
	public void visit(PlainSelect plainSelect) {
//		Transformation transformation = new Transformation(0, plainSelect);
//		transformation.transform();
//		this.transformationData.add(transformation);
	}

	@Override
	public void visit(SubSelect subSelect) {
		if (subSelect.getWithItemsList() != null) {
			throw new QueryException(NOT_SUPPORTED_YET + " With items query expressions.");
//			for (WithItem withItem : subSelect.getWithItemsList()) {
//				withItem.accept(this);
//			}
		}
		if (subSelect.getSelectBody() instanceof WithItem) {
			throw new QueryException(NOT_SUPPORTED_YET + " With items query expressions.");
		}
		subSelect.getSelectBody().accept(this);
	}

	private final class Transformation<P> implements SelectQueryVisitors {
		private int deep;

		private List<SelectItem> selectItems = Lists.newArrayList();
		private List<Column> columns = Lists.newArrayList();
		private List<Function> functions = Lists.newArrayList();

		private boolean isSelectExpressionPresent = false;
		private boolean wrapperRequired = false;

		private P parent;
		private Class parentClass;

		private boolean joinTable = false;
		private PlainSelect plainSelect;

		private TableTransformationData fromTable;
		private List<TableTransformationData> joinTables;

		private String alias;
		private Transformation subTransformation;


		private Transformation(int deep, P parent) {
			this.deep = deep;
			this.parent = parent;
			this.parentClass = this.parent.getClass();
		}

		public void transform() {
			try {
				if (plainSelect.getFromItem() != null) {
					plainSelect.getFromItem().accept(this);
				}

				if (plainSelect.getSelectItems() != null) {
					for (SelectItem item : Lists.newArrayList(plainSelect.getSelectItems())) { // to avoid concurrent modification exception
						item.accept(this);
					}
				}

				if (plainSelect.getJoins() != null) {
					joinTable = true;
					for (Join join : plainSelect.getJoins()) {
						join.getRightItem().accept(this);
					}
				}
				if (plainSelect.getWhere() != null) {
					plainSelect.getWhere().accept(this);
				}

				if (plainSelect.getHaving() != null) {
					plainSelect.getHaving().accept(this);
				}

				if (plainSelect.getOracleHierarchical() != null) {
					plainSelect.getOracleHierarchical().accept(this);
				}

				if (plainSelect.getOrderByElements() != null){
					for (OrderByElement orderByElement : plainSelect.getOrderByElements()) {
						orderByElement.accept(this);
					}
				}

//				if (plainSelect.getGroupByColumnReferences() != null){
//					for (Expression expression : plainSelect.getGroupByColumnReferences()) {
//						expression.accept(this);
//					}
//				}

				applyChanges();
			} catch (Exception e) {
				LOG.error(String.format("Transformation exception PlainSelect: [%s]; deep: [%s]", plainSelect.toString(), deep), e);
				throw e;
			}
		}

		protected void applyChanges() {
			if(wrapperRequired){
				PlainSelect clone = helper.clone(plainSelect);
				SubSelect subSelect = new SubSelect();
				subSelect.setAlias(createAlias(fromTable != null ? fromTable.getTableName() : "wrap_subs"));
				subSelect.setSelectBody(createInheritanceOperationList());
				clone.setFromItem(subSelect);
				clone.setWhere(null);


				if (parentClass.isAssignableFrom(Select.class)) {
					((Select) parent).setSelectBody(clone);
				}
				if (parentClass.isAssignableFrom(SubSelect.class)) {
					((SubSelect) parent).setSelectBody(clone);
				}

			}else{
				if (fromTable != null && fromTable.hasInheritance()) {
					if (parentClass.isAssignableFrom(Select.class)) {
						((Select) parent).setSelectBody(createInheritanceOperationList());
					}
					if (parentClass.isAssignableFrom(SubSelect.class)) {
						((SubSelect) parent).setSelectBody(createInheritanceOperationList());
					}
				}
			}

		}

		private Alias createAlias(String prefix) {
			return new Alias(StringUtils.isBlank(alias) ? "alias_"+prefix+"_"+deep : alias);
		}

		@Override
		public void visit(SelectExpressionItem item) {
			selectItems.add(item);
			if(!this.isSelectExpressionPresent){
				this.isSelectExpressionPresent = true;
			}
			item.getExpression().accept(this);
		}

		@Override
		public void visit(Function function) {
			if(!this.wrapperRequired){
				this.wrapperRequired = true;
			}
			ExpressionList exprList = function.getParameters();
			if (exprList != null) {
				visit(exprList);
			}
		}

		private SetOperationList createInheritanceOperationList() {
			SetOperationList setOperationList = new SetOperationList();
			List<SelectBody> bodies = Lists.newArrayList();
			List<Boolean> brackets = Lists.newArrayList(FALSE);
			List<SetOperation> setOperations = Lists.newArrayList();
			fromTable.getInheritance().orElseGet(Collections::emptySet)
					.stream().filter(metaTypeItem -> !metaTypeItem.getTableName().equals(fromTable.getTypeItemBind().getTableName()))
					.forEach(metaTypeItem -> {
						if (LOG.isDebugEnabled()) {
							LOG.debug(String.format("Add union for subtype [%s]", metaTypeItem.getTypeCode()));
						}

						PlainSelect newPlainSelect = helper.amendTable(helper.clone(plainSelect), metaTypeItem);

						newPlainSelect.setSelectItems(generateSelectItems(metaTypeItem, newPlainSelect));

						adjustWhere(newPlainSelect, metaTypeItem);

						bodies.add(newPlainSelect);
						brackets.add(FALSE);
						UnionOp union = new UnionOp();
						union.setAll(true);
						setOperations.add(union);
					});
			plainSelect.setSelectItems(generateSelectItems(fromTable.getTypeItemBind(), plainSelect));
			adjustWhere(plainSelect, fromTable.getTypeItemBind());
			helper.amendTable(plainSelect, fromTable.getTypeItemBind());
			bodies.add(plainSelect);

			setOperationList.setBracketsOpsAndSelects(brackets, bodies, setOperations);
			return setOperationList;
		}

		private List<SelectItem> generateSelectItems(MetaTypeItem metaTypeItem, PlainSelect plainSelect) {
			if(this.wrapperRequired && isSelectExpressionPresent) {

				return plainSelect.getSelectItems().stream().peek(selectItem -> {
					if(selectItem instanceof SelectExpressionItem && ((SelectExpressionItem) selectItem).getExpression() instanceof Function){
						Function f = (Function) ((SelectExpressionItem) selectItem).getExpression();
						if(f.isAllColumns()){
							((SelectExpressionItem) selectItem).setExpression(new Column((Table) plainSelect.getFromItem(), "uuid"));
						}
					}
				}).collect(Collectors.toList());
			} else {
				return plainSelect.getSelectItems();
			}
		}

		private void adjustWhere(PlainSelect newPlainSelect, MetaTypeItem metaTypeItem) {
			if (Objects.nonNull(newPlainSelect.getWhere())) {
				Expression left = new Column((Table) newPlainSelect.getFromItem(), cortexContext.findAttribute(metaTypeItem.getTypeCode(), GenericItem.META_TYPE).getColumnName());

				Set<MetaTypeItem> subTypeItemSet = CollectionUtils.isNotEmpty(metaTypeItem.getSubtypes()) ? metaTypeItem.getSubtypes().stream()
						.filter(sub -> sub.getTableName().equals(metaTypeItem.getTableName()))
						.collect(Collectors.toSet()) : null;

				Expression whereExpression = helper.createMetaEqExpression(metaTypeItem, left, subTypeItemSet);
				AndExpression andExpression = new AndExpression(newPlainSelect.getWhere(), whereExpression);
				newPlainSelect.setWhere(andExpression);

			} else {

				Expression left = new Column((Table) newPlainSelect.getFromItem(), cortexContext.findAttribute(metaTypeItem.getTypeCode(), GenericItem.META_TYPE).getColumnName());

				Set<MetaTypeItem> subTypeItemSet = CollectionUtils.isNotEmpty(metaTypeItem.getSubtypes()) ? metaTypeItem.getSubtypes().stream()
						.filter(sub -> sub.getTableName().equals(metaTypeItem.getTableName()))
						.collect(Collectors.toSet()) : null;

				Expression whereExpression = helper.createMetaEqExpression(metaTypeItem, left, subTypeItemSet);
				newPlainSelect.setWhere(whereExpression);
			}
		}


		public int getDeep() {
			return deep;
		}

		public PlainSelect getPlainSelect() {
			return plainSelect;
		}

		@Override
		public void visit(SubJoin subjoin) {
			subjoin.getLeft().accept(this);
			joinTable = true;
			for (Join join : subjoin.getJoinList()) {
				join.getRightItem().accept(this);
			}
			joinTable = false;
		}

		@Override
		public void visit(AllColumns tableColumn) {
			selectItems.add(tableColumn);
			if (fromTable != null && fromTable.hasInheritance()) {
				PlainSelect parent = (PlainSelect) ((SimpleNode) (tableColumn.getASTNode().jjtGetParent())).jjtGetValue();
				List<SelectItem> selectItems = parent.getSelectItems();
				selectItems.clear();

				cortexContext.getAllAttributes(fromTable.getTypeItemBind())
						.values().forEach(attribute -> {
					if (isNoneBlank(attribute.getColumnName())) {
						SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
						selectExpressionItem.setExpression(new Column((Table) plainSelect.getFromItem(), attribute.getColumnName()));
						selectItems.add(selectExpressionItem);
					}
				});
			}

		}

		@Override
		public void visit(AllTableColumns tableColumn) {
			selectItems.add(tableColumn);
			MetaTypeItem tableRelatedMetaType = Objects.nonNull(fromTable) ? cortexContext.findMetaType(getTable(tableColumn.getTable()).getFullyQualifiedName()) : null;
			if (Objects.nonNull(tableRelatedMetaType) && CollectionUtils.isNotEmpty(tableRelatedMetaType.getSubtypes())) {
				PlainSelect parent = (PlainSelect) ((SimpleNode) (tableColumn.getASTNode().jjtGetParent())).jjtGetValue();
				List<SelectItem> selectItems = parent.getSelectItems();
				selectItems.clear();

				cortexContext.getAllAttributes(tableRelatedMetaType)
						.values().forEach(attribute -> {
					if (isNoneBlank(attribute.getColumnName())) {
						SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
						selectExpressionItem.setExpression(new Column((Table) plainSelect.getFromItem(), attribute.getColumnName()));
						selectItems.add(selectExpressionItem);
					}
				});
			}
		}

		private Table getTable(Table table) {
			if(Objects.nonNull(alias) && Objects.nonNull(table) && !table.getName().equals(alias)){
				return table;
			}
			if(Objects.nonNull(fromTable)){
				return fromTable.getTable();
			}
			if(Objects.nonNull(subTransformation)){
				TableTransformationData subTable = subTransformation.getFromTable();
				if(Objects.nonNull(subTable) ){
					return subTable.getTable();
				}
				return subTransformation.getTable(table);
			}
			return null;
		}

		@Override
		public void visit(Column tableColumn) {
			columns.add(tableColumn);
			MetaAttributeTypeItem metaAttributeTypeItem = cortexContext.findAttribute(getTable(tableColumn.getTable()).getFullyQualifiedName(), tableColumn.getColumnName());
			if (Objects.nonNull(metaAttributeTypeItem)) {
				tableColumn.setColumnName(metaAttributeTypeItem.getColumnName());
			}
		}

		@Override
		public void visit(PlainSelect plainSelect) {
			this.plainSelect = plainSelect;
			transform();
		}

		@Override
		public void visit(Table table) {
			if (joinTable) {
				addJoinTable(table);
			} else {
				fromTable = helper.bindItem(table);
			}
		}

		private void addJoinTable(Table table) {
			if (CollectionUtils.isEmpty(joinTables)) {
				joinTables = Lists.newArrayList();
			}
			joinTables.add(helper.bindItem(table));
		}

		@Override
		public void visit(SubSelect subSelect) {
			if (subSelect.getWithItemsList() != null) {
				throw new QueryException(NOT_SUPPORTED_YET + " With items query expressions.");
//			for (WithItem withItem : subSelect.getWithItemsList()) {
//				withItem.accept(this);
//			}
			}
			if (subSelect.getSelectBody() instanceof WithItem) {
				throw new QueryException(NOT_SUPPORTED_YET + " With items query expressions.");
			}
			subTransformation = new Transformation<>(deep + 1, subSelect);
			alias = subSelect.getAlias().getName();
			subSelect.getSelectBody().accept(subTransformation);
		}

		@Override
		public void visit(SetOperationList list) {
			for (SelectBody plainSelect : list.getSelects()) {
				plainSelect.accept(new Transformation<>(deep + 1, plainSelect));
			}
		}

		@Override
		public void visit(LateralSubSelect lateralSubSelect) {
			SubSelect subSelect = lateralSubSelect.getSubSelect();
			subSelect.getSelectBody().accept(new Transformation<>(deep + 1, subSelect));
		}

		@Override
		public void visit(AllComparisonExpression allComparisonExpression) {
			SubSelect subSelect = allComparisonExpression.getSubSelect();
			subSelect.getSelectBody().accept(new Transformation<>(deep + 1, subSelect));
		}

		@Override
		public void visit(AnyComparisonExpression anyComparisonExpression) {
			SubSelect subSelect = anyComparisonExpression.getSubSelect();
			subSelect.getSelectBody().accept(new Transformation<>(deep + 1, subSelect));
		}

		public TableTransformationData getFromTable() {
			return fromTable;
		}
	}

}

package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.data.TableTransformationData;
import com.coretex.core.activeorm.query.select.scanners.FromItemScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.SelectItemDataInjectionPoint;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

public class SelectItemAllTransformationStrategy extends AbstractTransformationStrategy<SelectItemDataInjectionPoint, SelectBody> {

	public SelectItemAllTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		super(transformationStrategies, transformationHelper);
	}

	public SelectBody apply(SelectItemDataInjectionPoint dataInjectionPoint) {
		return dataInjectionPoint.getSelectBodyScannerOwner().map(selectBodyScanner -> {
			var infoHolder = new TransformInfoHolder(dataInjectionPoint, selectBodyScanner);
			if (transformationRequired(infoHolder)) {
				return amendForTable(infoHolder);
			}
			return selectBodyScanner.scannedObject();
		}).orElse(null);

	}

	private SelectBody amendForTable(TransformInfoHolder infoHolder) {
		var selectBody = (PlainSelect) infoHolder.getSelectBodyScanner().scannedObject();

		List<SelectItem> selectItems = selectBody.getSelectItems();
		selectItems.remove(infoHolder.getDataInjectionPoint().getScanner().scannedObject());

		getTransformationHelper().getCortexContext().getAllAttributes(infoHolder.getTableTransformationData().getTypeItemBind())
				.values().forEach(attribute -> {
			if (isNoneBlank(attribute.getColumnName())) {
				SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
				selectExpressionItem.setExpression(new Column(infoHolder.getTableTransformationData().getTable(), attribute.getColumnName()));
				selectItems.add(selectExpressionItem);
			}
		});
		return selectBody;
	}

	private boolean transformationRequired(TransformInfoHolder infoHolder) {
		if (infoHolder.getDataInjectionPoint().isTableAware()) {
			Table table = infoHolder.getDataInjectionPoint().getTable();
			var tableTransformationDataForTable = getTransformationHelper().getTableTransformationDataForTable(table, infoHolder.getSelectBodyScanner());
			infoHolder.setTableTransformationData(tableTransformationDataForTable);
			var transform = Objects.nonNull(tableTransformationDataForTable) && tableTransformationDataForTable.isBind();
			if(Objects.nonNull(tableTransformationDataForTable)){
				infoHolder.getDataInjectionPoint().getContext().addAllItemUsed(tableTransformationDataForTable.getUsedTypes());
			}
			return transform;
		}

		var fromItemScanner = infoHolder.getFromItemScanner();
		return fromItemScanner.isPresent() && fromItemScanner.map(is -> {
			infoHolder.setTableTransformationData(is.getTableTransformationData());
			infoHolder.getDataInjectionPoint().getContext().addAllItemUsed(is.getTableTransformationData().getUsedTypes());
			return Objects.nonNull(infoHolder.getTableTransformationData()) &&
					infoHolder.getTableTransformationData().isBind();
		}).get();
	}

	private class TransformInfoHolder {
		private SelectItemDataInjectionPoint dataInjectionPoint;
		private SelectBodyScanner selectBodyScanner;
		private TableTransformationData tableTransformationData;

		private TransformInfoHolder(SelectItemDataInjectionPoint dataInjectionPoint, SelectBodyScanner selectBodyScanner) {
			this.dataInjectionPoint = dataInjectionPoint;
			this.selectBodyScanner = selectBodyScanner;
		}

		public SelectItemDataInjectionPoint getDataInjectionPoint() {
			return dataInjectionPoint;
		}

		public SelectBodyScanner getSelectBodyScanner() {
			return selectBodyScanner;
		}

		public Optional<FromItemScanner> getFromItemScanner() {
			return Optional.ofNullable(selectBodyScanner.getFromItemScanner());
		}

		public TableTransformationData getTableTransformationData() {
			return tableTransformationData;
		}

		public void setTableTransformationData(TableTransformationData tableTransformationData) {
			this.tableTransformationData = tableTransformationData;
		}
	}
}

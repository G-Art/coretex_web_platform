package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.operations.contexts.UpdateOperationConfigContext;
import com.coretex.core.activeorm.query.operations.dataholders.UpdateValueDataHolder;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.meta.AbstractGenericItem;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.coretex.core.general.utils.ItemUtils.getTypeCode;

public class UpdateOperationSpec extends ModificationOperationSpec<Update, UpdateOperationSpec, UpdateOperationConfigContext> {

	private final static String UPDATE_ITEM_QUERY = "update %s set %s where uuid = :uuid";

	private Map<String, UpdateValueDataHolder> updateValueDatas;

	public UpdateOperationSpec(GenericItem item) {
		super(item);
		this.setQuerySupplier(this::createUpdateQuery);
	}

	public UpdateOperationSpec(GenericItem item, boolean cascade) {
		super(item, cascade);
		this.setQuerySupplier(this::createUpdateQuery);
	}

	public UpdateOperationSpec(GenericItem item, boolean cascade, boolean transactional) {
		super(item, cascade, transactional);
		this.setQuerySupplier(this::createUpdateQuery);
	}

	private String createUpdateQuery() {
		getItem().setUpdateDate(LocalDateTime.now());

		Map<String, UpdateValueDataHolder> saveColumnValues = getAllAttributes().entrySet().stream()
				.filter(entry -> !(entry.getValue().getAttributeType() instanceof MetaRelationTypeItem))
				.filter(entry -> StringUtils.isNoneBlank(entry.getValue().getColumnName()))
				.filter(entry -> getItem().getItemContext().isDirty(entry.getKey()) ||
						Objects.nonNull(entry.getValue().getDefaultValue()) ||
						!entry.getValue().getOptional())
				.collect(Collectors.toMap(entry -> entry.getValue().getColumnName(), entry -> new UpdateValueDataHolder( entry.getValue(), this)));

		updateValueDatas = saveColumnValues.entrySet()
				.stream()
				.collect(Collectors.toMap(entry -> entry.getValue().getAttributeName(), Map.Entry::getValue));

		Set<Map.Entry<String, UpdateValueDataHolder>> entries = saveColumnValues.entrySet();
		updateValueDatas.put(AbstractGenericItem.UUID, new UpdateValueDataHolder(getMetaTypeProvider().findAttribute(getTypeCode(getItem()), AbstractGenericItem.UUID), this));

		return String.format(UPDATE_ITEM_QUERY, getItem().getMetaType().getTableName(),
				entries.stream().map(e -> String.format("%s = :%s", e.getKey(), e.getValue().getAttributeName())).collect(Collectors.joining(",")));
	}

	public Map<String, UpdateValueDataHolder> getUpdateValueDatas() {
		return updateValueDatas;
	}

	@Override
	public UpdateOperationConfigContext createOperationContext() {
		return new UpdateOperationConfigContext(this);
	}
}

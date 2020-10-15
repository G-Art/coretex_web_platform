package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.InsertOperation;
import com.coretex.core.activeorm.query.operations.dataholders.InsertValueDataHolder;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.meta.AbstractGenericItem;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.coretex.core.general.utils.ItemUtils.getTypeCode;

public class InsertOperationSpec extends ModificationOperationSpec<Insert, InsertOperation> {

	protected final static String INSERT_ITEM_QUERY = "insert into %s (%s) values (%s)";

	private Map<String, InsertValueDataHolder> insertValueDatas;
	private UUID newUuid;


	public InsertOperationSpec(GenericItem item) {
		super(item);
		this.setQuerySupplier(this::buildQuery);
	}

	public InsertOperationSpec(GenericItem item, boolean cascade) {
		super(item, cascade);
		this.setQuerySupplier(this::buildQuery);
	}

	public InsertOperationSpec(GenericItem item, boolean cascade, boolean transactional) {
		super(item, cascade, transactional);
		this.setQuerySupplier(this::buildQuery);
	}

	private String buildQuery() {
		this.newUuid = UUID.randomUUID();
		LocalDateTime creationDate = LocalDateTime.now();
		getItem().setCreateDate(creationDate);
		getItem().setUpdateDate(creationDate);

		Map<String, InsertValueDataHolder> saveColumnValues = getAllAttributes().entrySet().stream()
				.filter(entry -> !(entry.getValue().getAttributeType() instanceof MetaRelationTypeItem))
				.filter(entry -> StringUtils.isNoneBlank(entry.getValue().getColumnName()))
				.filter(entry -> getItem().getItemContext().isDirty(entry.getKey()) ||
						Objects.nonNull(entry.getValue().getDefaultValue()) ||
						!entry.getValue().getOptional())
				.collect(Collectors.toMap(entry -> entry.getValue().getColumnName(), entry -> InsertOperation.createInsertValueDataHolder(this, entry.getValue())));

		saveColumnValues.put(AbstractGenericItem.UUID, InsertOperation.createInsertValueDataHolder(this, getMetaTypeProvider().findAttribute(getTypeCode(getItem()), AbstractGenericItem.UUID)));

		insertValueDatas = saveColumnValues.entrySet()
				.stream()
				.collect(Collectors.toMap(entry -> entry.getValue().getAttributeName(), Map.Entry::getValue));

		Set<Map.Entry<String, InsertValueDataHolder>> entries = saveColumnValues.entrySet();

		return String.format(INSERT_ITEM_QUERY, getItem().getMetaType().getTableName(),
				entries.stream().map(Map.Entry::getKey).collect(Collectors.joining(",")),
				entries.stream()
						.map(entry -> String.format(":%s", entry.getValue().getAttributeName()))
						.collect(Collectors.joining(",")));
	}


	public UUID getNewUuid() {
		return newUuid;
	}

	@Override
	public InsertOperation createOperation(QueryTransformationProcessor<Insert> processor) {
		return new InsertOperation(this);
	}


	public Map<String, InsertValueDataHolder> getInsertValueDatas() {
		return insertValueDatas;
	}
}

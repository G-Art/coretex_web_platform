package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.LocalizedDataRemoveOperation;
import com.coretex.core.activeorm.query.operations.ModificationOperation;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.items.core.MetaAttributeTypeItem;
import net.sf.jsqlparser.statement.Statement;

import java.util.HashMap;
import java.util.Map;

public class LocalizedDataRemoveOperationSpec extends ModificationOperationSpec<Statement, LocalizedDataRemoveOperation> {

	private final static String DELETE_LOCALIZED_DATA_QUERY = "delete from %s_LOC where owner = :owner and attribute = :attribute";

	private MetaAttributeTypeItem attributeTypeItem;
	private Map<String, Object> params = new HashMap<>();


	public LocalizedDataRemoveOperationSpec(ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator, MetaAttributeTypeItem attributeTypeItem) {
		super(initiator.getOperationSpec().getItem());
		setNativeQuery(false);
		this.attributeTypeItem = attributeTypeItem;
		this.setQuerySupplier(this::buildQuery);
	}

	private String buildQuery() {
		if(AttributeTypeUtils.isRelationAttribute(attributeTypeItem)){
			throw new IllegalArgumentException(String.format("Relation attribute cant be localized [name: %s] [owner: %s]", attributeTypeItem.getAttributeName(), attributeTypeItem.getOwner().getTypeCode()));
		}
		params.put("owner", getItem().getUuid());
		params.put("attribute", getAttributeTypeItem().getUuid());

		return String.format(DELETE_LOCALIZED_DATA_QUERY, getItem().getMetaType().getTableName());
	}

	public Map<String, Object> getParams() {
		return params;
	}

	@Override
	public void flush() {
		//ignored
	}

	public MetaAttributeTypeItem getAttributeTypeItem() {
		return attributeTypeItem;
	}

	@Override
	public LocalizedDataRemoveOperation createOperation(QueryTransformationProcessor processor) {
		return new LocalizedDataRemoveOperation(this);
	}

	@Override
	public boolean constraintsApplicable() {
		return false;
	}
}

package com.coretex.core.activeorm.query.operations.dataholders;

import com.coretex.core.activeorm.exceptions.MandatoryAttributeException;
import com.coretex.core.activeorm.query.specs.ModificationOperationSpec;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.meta.AbstractGenericItem;
import org.springframework.jdbc.core.SqlParameterValue;

import java.util.Objects;

import static com.coretex.core.general.utils.ItemUtils.getTypeCode;
import static com.coretex.core.general.utils.OperationUtils.isLoopSafe;
import static java.util.Objects.nonNull;

public abstract class AbstractValueDataHolder<S extends ModificationOperationSpec> {

	private S operationSpec;
	private MetaAttributeTypeItem attributeTypeItem;
	private Boolean itemRelation;
	private GenericItem relatedItem;

	public AbstractValueDataHolder(S operationSpec, MetaAttributeTypeItem attributeTypeItem) {
		this.operationSpec = operationSpec;
		this.attributeTypeItem = attributeTypeItem;

		this.itemRelation = attributeTypeItem.getAttributeType() instanceof MetaTypeItem;
		if(itemRelation) {
			this.relatedItem = ((GenericItem)getItem().getAttributeValue(attributeTypeItem.getAttributeName()));
		}
	}

	protected S getOperationSpec() {
		return operationSpec;
	}

	public Boolean isMandatory(){
		return !attributeTypeItem.getOptional();
	}

	public MetaAttributeTypeItem getAttributeTypeItem() {
		return attributeTypeItem;
	}
	public String getAttributeName(){
		return attributeTypeItem.getAttributeName();
	}

	protected MetaTypeProvider getMetaTypeProvider(){
		return operationSpec.getMetaTypeProvider();
	}

	public GenericItem getItem() {
		return operationSpec.getItem();
	}

	public SqlParameterValue createSqlParameterValue(){
		Object value = getValue();

		if(Objects.isNull(value) && isMandatory()){
			throw new MandatoryAttributeException(String.format("Mandatory attribute [%s::%s] is not defined", attributeTypeItem.getOwner().getTypeCode(), attributeTypeItem.getAttributeName()));
		}

		return new SqlParameterValue(getSqlType(), typeName(), value instanceof Class ? ((Class) value).getCanonicalName() : value);
	}

	private String typeName() {
		return getMetaTypeProvider().getSqlTypeName(getRegularType(getAttributeTypeItem().getAttributeType()));
	}

	private int getSqlType() {
		return getMetaTypeProvider().getSqlType(getRegularType(getAttributeTypeItem().getAttributeType()));
	}

	private RegularTypeItem getRegularType(GenericItem attributeType) {
		if(attributeType instanceof MetaTypeItem || attributeType instanceof MetaEnumTypeItem){
			return (RegularTypeItem) getMetaTypeProvider().findAttribute(getTypeCode(attributeType), AbstractGenericItem.UUID).getAttributeType();
		}
		return (RegularTypeItem) attributeType;
	}

	public Boolean isItemRelation() {
		return itemRelation;
	}

	public GenericItem getRelatedItem() {
		return relatedItem;
	}

	public abstract Object getValue();


	public boolean availableForBeforeExecution(){
		return nonNull(getRelatedItem()) &&
				(getRelatedItem().getItemContext().isDirty() || getRelatedItem().getItemContext().isNew()) &&
				isLoopSafe(getOperationSpec(), getRelatedItem());
	}
}

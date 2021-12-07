package com.coretex.data;

import com.coretex.core.general.utils.ItemUtils;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_SET;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MetaTypeDTO {

	private final String parent;
	private String parentUUID;
	private Boolean sharedTable;
	private String uuid;
	private String typeCode;
	private String description;
	private String tableName;
	private String createDate;
	private String updateDate;
	private String metaType;
	private String itemClass;
	private Boolean tableOwner;
	private Boolean abstractType;
	private Collection<String> strictSubtypes;
	private Collection<String> subtypes;
	private Collection<String> itemAttributes;


	public MetaTypeDTO(MetaTypeItem metaTypeItem) {
		this.uuid = metaTypeItem.getUuid().toString();
		this.abstractType = metaTypeItem.getAbstract();
		this.typeCode = metaTypeItem.getTypeCode();
		this.description = metaTypeItem.getDescription();
		this.tableName = metaTypeItem.getTableName();
		this.tableOwner = metaTypeItem.getTableOwner();
		this.createDate = metaTypeItem.getCreateDate().toString();
		this.updateDate = metaTypeItem.getUpdateDate().toString();
		this.sharedTable = Objects.nonNull(metaTypeItem.getSubtypes()) && metaTypeItem.getSubtypes().stream().anyMatch(item -> !item.getTableOwner());
		this.metaType = metaTypeItem.getMetaType() != null ? metaTypeItem.getMetaType().getTypeCode() : EMPTY;
		this.itemClass = metaTypeItem.getItemClass().getCanonicalName();
		this.parent = Objects.nonNull(metaTypeItem.getParent()) ? metaTypeItem.getParent().getTypeCode() : EMPTY;
		this.parentUUID = Objects.nonNull(metaTypeItem.getParent()) ? metaTypeItem.getParent().getUuid().toString() : EMPTY;
		this.strictSubtypes = Objects.nonNull(metaTypeItem.getSubtypes()) ? metaTypeItem.getSubtypes()
				.stream()
				.map(MetaTypeItem::getTypeCode)
				.collect(Collectors.toSet()) : EMPTY_SET;
		this.subtypes = Objects.nonNull(metaTypeItem.getSubtypes()) ? ItemUtils.getAllSubtypes(metaTypeItem)
				.stream()
				.map(MetaTypeItem::getTypeCode)
				.collect(Collectors.toSet()) : EMPTY_SET;
		this.itemAttributes = Objects.nonNull(metaTypeItem.getItemAttributes()) ? metaTypeItem.getItemAttributes()
				.stream()
				.map(MetaAttributeTypeItem::getAttributeName)
				.collect(Collectors.toSet()) : EMPTY_SET;
	}

	public String getParentUUID() {
		return parentUUID;
	}

	public void setParentUUID(String parentUUID) {
		this.parentUUID = parentUUID;
	}

	public Boolean getSharedTable() {
		return sharedTable;
	}

	public void setSharedTable(Boolean sharedTable) {
		this.sharedTable = sharedTable;
	}

	public Boolean getAbstractType() {
		return abstractType;
	}

	public void setAbstractType(Boolean abstractType) {
		this.abstractType = abstractType;
	}

	public String getParent() {
		return parent;
	}

	public Collection<String> getSubtypes() {
		return subtypes;
	}

	public void setSubtypes(Collection<String> subtypes) {
		this.subtypes = subtypes;
	}

	public Collection<String> getStrictSubtypes() {
		return strictSubtypes;
	}

	public void setStrictSubtypes(Collection<String> strictSubtypes) {
		this.strictSubtypes = strictSubtypes;
	}

	public Collection<String> getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(Collection<String> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}

	public Boolean getTableOwner() {
		return tableOwner;
	}

	public void setTableOwner(Boolean tableOwner) {
		this.tableOwner = tableOwner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getMetaType() {
		return metaType;
	}

	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}

	public String getItemClass() {
		return itemClass;
	}

	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}
}

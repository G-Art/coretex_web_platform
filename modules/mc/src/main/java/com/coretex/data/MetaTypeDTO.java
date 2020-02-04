package com.coretex.data;

import com.coretex.core.general.utils.ItemUtils;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class MetaTypeDTO {

	private final String parent;
	private String uuid;
	private String typeCode;
	private String description;
	private String tableName;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private String metaType;
	private String itemClass;
	private Boolean tableOwner;
	private Collection<String> subtypes;
	private Collection<String> itemAttributes;


	public MetaTypeDTO(MetaTypeItem metaTypeItem) {
		this.uuid = metaTypeItem.getUuid().toString();
		this.typeCode = metaTypeItem.getTypeCode();
		this.description = metaTypeItem.getDescription();
		this.tableName = metaTypeItem.getTableName();
		this.tableOwner = metaTypeItem.getTableOwner();
		this.createDate = LocalDateTime.from(metaTypeItem.getCreateDate());
		this.updateDate = LocalDateTime.from(metaTypeItem.getUpdateDate());
		this.metaType = metaTypeItem.getMetaType() != null ? metaTypeItem.getMetaType().getTypeCode() : StringUtils.EMPTY;
		this.itemClass = metaTypeItem.getItemClass().getCanonicalName();
		this.parent = Objects.nonNull(metaTypeItem.getParent()) ?  metaTypeItem.getParent().getTypeCode() : "";
		this.subtypes = Objects.nonNull(metaTypeItem.getSubtypes()) ? ItemUtils.getAllSubtypes(metaTypeItem)
				.stream()
				.map(MetaTypeItem::getTypeCode)
				.collect(Collectors.toSet()) : Collections.EMPTY_SET;
		this.itemAttributes = Objects.nonNull(metaTypeItem.getItemAttributes()) ? metaTypeItem.getItemAttributes()
				.stream()
				.map(MetaAttributeTypeItem::getAttributeName)
				.collect(Collectors.toSet()) : Collections.EMPTY_SET;
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

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
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

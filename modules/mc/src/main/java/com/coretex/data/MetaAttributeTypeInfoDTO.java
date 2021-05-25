package com.coretex.data;

import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;

import java.util.Objects;

public class MetaAttributeTypeInfoDTO {

	private String uuid;
	private String name;
	private String description;
	private Boolean localized;
	private boolean relation = false;
	private boolean collection = false;;

	public MetaAttributeTypeInfoDTO(MetaAttributeTypeItem metaTypeItem) {
		this.uuid = metaTypeItem.getUuid().toString();
		this.name = metaTypeItem.getAttributeName();
		this.description = metaTypeItem.getDescription();
		this.localized = metaTypeItem.getLocalized();
		this.relation = metaTypeItem.getAttributeType() instanceof MetaRelationTypeItem;
		this.collection = Objects.nonNull(metaTypeItem.getContainerType());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getLocalized() {
		return localized;
	}

	public void setLocalized(Boolean localized) {
		this.localized = localized;
	}

	public boolean isRelation() {
		return relation;
	}

	public void setRelation(boolean relation) {
		this.relation = relation;
	}

	public boolean isCollection() {
		return collection;
	}

	public void setCollection(boolean collection) {
		this.collection = collection;
	}
}

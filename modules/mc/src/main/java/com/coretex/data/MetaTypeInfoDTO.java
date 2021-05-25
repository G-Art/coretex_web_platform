package com.coretex.data;

import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class MetaTypeInfoDTO {

	private String uuid;
	private String typeCode;
	private String description;
	private Collection<MetaAttributeTypeInfoDTO> itemAttributes;

	public MetaTypeInfoDTO(MetaTypeItem metaTypeItem, Collection<MetaAttributeTypeItem> allAttributes) {
		this.uuid = metaTypeItem.getUuid().toString();
		this.typeCode = metaTypeItem.getTypeCode();
		this.description = metaTypeItem.getDescription();
		this.itemAttributes = CollectionUtils.isNotEmpty(allAttributes) ? allAttributes
				.stream()
				.map(MetaAttributeTypeInfoDTO::new)
				.collect(Collectors.toSet()) : Collections.EMPTY_SET;
	}

	public Collection<MetaAttributeTypeInfoDTO> getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(Collection<MetaAttributeTypeInfoDTO> itemAttributes) {
		this.itemAttributes = itemAttributes;
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

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}

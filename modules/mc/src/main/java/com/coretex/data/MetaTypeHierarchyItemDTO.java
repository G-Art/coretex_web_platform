package com.coretex.data;

import java.util.List;
import java.util.UUID;

public class MetaTypeHierarchyItemDTO {
	private UUID uuid;
	private String typeCode;
	private Boolean readOnly;
	private List<MetaTypeHierarchyItemDTO> subtypes;

	public MetaTypeHierarchyItemDTO(UUID uuid, String typeCode) {
		this.uuid = uuid;
		this.typeCode = typeCode;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public List<MetaTypeHierarchyItemDTO> getSubtypes() {
		return subtypes;
	}

	public void setSubtypes(List<MetaTypeHierarchyItemDTO> subtypes) {
		this.subtypes = subtypes;
	}
}

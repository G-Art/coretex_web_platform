package com.coretex.data;

public class MetaTypeHierarchyDTO {
	private MetaTypeHierarchyItemDTO root;

	public MetaTypeHierarchyDTO(MetaTypeHierarchyItemDTO root) {
		this.root = root;
	}

	public MetaTypeHierarchyItemDTO getRoot() {
		return root;
	}
}

package com.coretex.shop.model.security;

import java.util.UUID;

/**
 * Object used for reading a group
 *
 * @author carlsamson
 */
public class ReadableGroup extends GroupEntity {


	private static final long serialVersionUID = 1L;

	private UUID id;

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}


}

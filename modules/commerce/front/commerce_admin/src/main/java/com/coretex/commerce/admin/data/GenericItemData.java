package com.coretex.commerce.admin.data;

import java.io.Serializable;
import java.util.UUID;

public class GenericItemData implements Serializable {
	private UUID uuid;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}

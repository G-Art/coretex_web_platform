package com.coretex.shop.admin.data;

import java.io.Serializable;
import java.util.UUID;

public abstract class GenericDto implements Serializable {
	private UUID uuid;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}

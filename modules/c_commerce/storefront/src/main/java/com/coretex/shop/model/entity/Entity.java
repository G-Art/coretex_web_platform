package com.coretex.shop.model.entity;

import java.io.Serializable;
import java.util.UUID;

public class Entity implements Serializable {

	public Entity() {
	}


	private static final long serialVersionUID = 1L;
	private UUID uuid;

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

}

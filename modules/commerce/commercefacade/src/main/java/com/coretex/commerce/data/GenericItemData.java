package com.coretex.commerce.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class GenericItemData implements Serializable {
	private UUID uuid;
	private LocalDateTime createDate;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
}

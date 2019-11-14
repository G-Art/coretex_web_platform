package com.coretex.core.model.catalog.product;

import java.util.List;
import java.util.UUID;

import com.coretex.core.model.catalog.product.attribute.AttributeCriteria;
import com.coretex.core.model.common.Criteria;

public class ProductCriteria extends Criteria {


	private String productName;
	private List<AttributeCriteria> attributeCriteria;


	private Boolean available = null;

	private List<UUID> categoryIds;
	private List<String> availabilities;
	private List<UUID> productIds;

	private String status;

	private UUID manufacturerId = null;

	private UUID ownerId = null;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}


	public List<UUID> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<UUID> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public List<String> getAvailabilities() {
		return availabilities;
	}

	public void setAvailabilities(List<String> availabilities) {
		this.availabilities = availabilities;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public void setAttributeCriteria(List<AttributeCriteria> attributeCriteria) {
		this.attributeCriteria = attributeCriteria;
	}

	public List<AttributeCriteria> getAttributeCriteria() {
		return attributeCriteria;
	}

	public void setProductIds(List<UUID> productIds) {
		this.productIds = productIds;
	}

	public List<UUID> getProductIds() {
		return productIds;
	}

	public void setManufacturerId(UUID manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public UUID getManufacturerId() {
		return manufacturerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UUID getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(UUID ownerId) {
		this.ownerId = ownerId;
	}


}

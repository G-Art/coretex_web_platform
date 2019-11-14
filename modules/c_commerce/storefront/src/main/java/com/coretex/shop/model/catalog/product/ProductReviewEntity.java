package com.coretex.shop.model.catalog.product;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotEmpty;

import com.coretex.shop.model.entity.ShopEntity;


public class ProductReviewEntity extends ShopEntity implements Serializable {


	private static final long serialVersionUID = 1L;
	@NotEmpty
	private String description;
	private UUID productId;
	private String date;

	@NotNull
	@Min(1)
	@Max(5)
	private Double rating;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


}

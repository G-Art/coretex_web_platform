package com.coretex.core.model.catalog.product.price;

import com.coretex.items.commerce_core_model.ProductPriceItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Transient entity used to display
 * different price information in the catalogue
 *
 * @author Carl Samson
 */
public class FinalPrice implements Serializable {


	private static final long serialVersionUID = 1L;
	private BigDecimal discountedPrice = null;//final price if a discount is applied
	private BigDecimal originalPrice = null;//original price
	private BigDecimal finalPrice = null;//final price discount or not
	private boolean discounted = false;
	private int discountPercent = 0;

	private Date discountEndDate = null;

	private boolean defaultPrice;
	private ProductPriceItem productPrice;
	List<FinalPrice> additionalPrices;

	public List<FinalPrice> getAdditionalPrices() {
		return additionalPrices;
	}

	public void setAdditionalPrices(List<FinalPrice> additionalPrices) {
		this.additionalPrices = additionalPrices;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}


	public int getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(int discountPercent) {
		this.discountPercent = discountPercent;
	}

	public Date getDiscountEndDate() {
		return discountEndDate;
	}

	public void setDiscountEndDate(Date discountEndDate) {
		this.discountEndDate = discountEndDate;
	}

	public boolean isDiscounted() {
		return discounted;
	}

	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}

	public void setDiscountedPrice(BigDecimal discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public BigDecimal getDiscountedPrice() {
		return discountedPrice;
	}


	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}

	public BigDecimal getFinalPrice() {
		return finalPrice;
	}

	public void setDefaultPrice(boolean defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	public boolean isDefaultPrice() {
		return defaultPrice;
	}

	public void setProductPrice(ProductPriceItem productPrice) {
		this.productPrice = productPrice;
	}

	public ProductPriceItem getProductPrice() {
		return productPrice;
	}

}

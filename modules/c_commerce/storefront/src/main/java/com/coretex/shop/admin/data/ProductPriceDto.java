package com.coretex.shop.admin.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class ProductPriceDto extends GenericDto {

	private String code;

	private Map<String, String> name;
	private Map<String, String> title;
	private Map<String, String> description;

	private BigDecimal productPriceAmount;
	private Date productPriceSpecialStartDate;
	private Date productPriceSpecialEndDate;
	private BigDecimal productPriceSpecialAmount;
	private Boolean defaultPrice;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, String> getName() {
		return name;
	}

	public void setName(Map<String, String> name) {
		this.name = name;
	}

	public Map<String, String> getTitle() {
		return title;
	}

	public void setTitle(Map<String, String> title) {
		this.title = title;
	}

	public Map<String, String> getDescription() {
		return description;
	}

	public void setDescription(Map<String, String> description) {
		this.description = description;
	}

	public BigDecimal getProductPriceAmount() {
		return productPriceAmount;
	}

	public void setProductPriceAmount(BigDecimal productPriceAmount) {
		this.productPriceAmount = productPriceAmount;
	}

	public Date getProductPriceSpecialStartDate() {
		return productPriceSpecialStartDate;
	}

	public void setProductPriceSpecialStartDate(Date productPriceSpecialStartDate) {
		this.productPriceSpecialStartDate = productPriceSpecialStartDate;
	}

	public Date getProductPriceSpecialEndDate() {
		return productPriceSpecialEndDate;
	}

	public void setProductPriceSpecialEndDate(Date productPriceSpecialEndDate) {
		this.productPriceSpecialEndDate = productPriceSpecialEndDate;
	}

	public BigDecimal getProductPriceSpecialAmount() {
		return productPriceSpecialAmount;
	}

	public void setProductPriceSpecialAmount(BigDecimal productPriceSpecialAmount) {
		this.productPriceSpecialAmount = productPriceSpecialAmount;
	}

	public Boolean getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(Boolean defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

}

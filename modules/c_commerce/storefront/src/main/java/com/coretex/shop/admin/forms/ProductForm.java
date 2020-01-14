package com.coretex.shop.admin.forms;

import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.shop.admin.data.ManufacturerDto;
import com.coretex.shop.admin.data.ProductAvailabilityDto;
import com.coretex.shop.admin.data.ProductPriceDto;
import com.coretex.shop.admin.data.ProductTypeDto;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class ProductForm implements Serializable {

	private UUID uuid;

	@NotBlank
	private String code;

	private Map<String, String> name;
	private Map<String, String> title;
	private Map<String, String> description;
	private Map<String, String> productHighlight;
	private Map<String, String> productExternalDl;
	private Map<String, String> metaTitle;
	private Map<String, String> metaKeywords;
	private Map<String, String> metaDescription;

	private boolean available = false;
	private boolean preOrder = false;
	private boolean productShippable = false;
	private String dateAvailable;
	private BigDecimal productWeight;
	private BigDecimal productHeight;
	private BigDecimal productWidth;
	private BigDecimal productLength;
	private Integer sortOrder;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, String> getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(Map<String, String> metaTitle) {
		this.metaTitle = metaTitle;
	}

	public Map<String, String> getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(Map<String, String> metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public Map<String, String> getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(Map<String, String> metaDescription) {
		this.metaDescription = metaDescription;
	}

	@NotEmpty
	private String productPrice = "0";

	@Valid
	private ManufacturerDto manufacturer;
	@Valid
	private ProductTypeDto type;
	@Valid
	private ProductAvailabilityDto availability;
	@Valid
	private ProductPriceDto price = null;

	private Integer productQuantity;
	private MultipartFile image = null;

	//provides wrapping to the main product entity


	private ProductImageItem productImage = null;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public BigDecimal getProductLength() {
		return productLength;
	}

	public void setProductLength(BigDecimal productLength) {
		this.productLength = productLength;
	}

	public BigDecimal getProductHeight() {
		return productHeight;
	}

	public void setProductHeight(BigDecimal productHeight) {
		this.productHeight = productHeight;
	}

	public BigDecimal getProductWidth() {
		return productWidth;
	}

	public void setProductWidth(BigDecimal productWidth) {
		this.productWidth = productWidth;
	}

	public BigDecimal getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(BigDecimal productWeight) {
		this.productWeight = productWeight;
	}

	public boolean isProductShippable() {
		return productShippable;
	}

	public void setProductShippable(boolean productShippable) {
		this.productShippable = productShippable;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public Map<String, String> getProductExternalDl() {
		return productExternalDl;
	}

	public void setProductExternalDl(Map<String, String> productExternalDl) {
		this.productExternalDl = productExternalDl;
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

	public Map<String, String> getProductHighlight() {
		return productHighlight;
	}

	public void setProductHighlight(Map<String, String> productHighlight) {
		this.productHighlight = productHighlight;
	}

	public ProductTypeDto getType() {
		return type;
	}

	public void setType(ProductTypeDto type) {
		this.type = type;
	}

	public ManufacturerDto getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(ManufacturerDto manufacturer) {
		this.manufacturer = manufacturer;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isPreOrder() {
		return preOrder;
	}

	public void setPreOrder(boolean preOrder) {
		this.preOrder = preOrder;
	}

	public String getDateAvailable() {
		return dateAvailable;
	}

	public void setDateAvailable(String dateAvailable) {
		this.dateAvailable = dateAvailable;
	}

	public ProductAvailabilityDto getAvailability() {
		return availability;
	}

	public void setAvailability(ProductAvailabilityDto availability) {
		this.availability = availability;
	}

	public ProductPriceDto getPrice() {
		return price;
	}

	public void setPrice(ProductPriceDto price) {
		this.price = price;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductImage(ProductImageItem productImage) {
		this.productImage = productImage;
	}

	public ProductImageItem getProductImage() {
		return productImage;
	}


}

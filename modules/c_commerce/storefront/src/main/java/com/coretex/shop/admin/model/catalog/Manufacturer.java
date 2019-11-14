package com.coretex.shop.admin.model.catalog;

import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


public class Manufacturer implements Serializable {


	private static final long serialVersionUID = -4531526676134574984L;

	//provides wrapping to the main ManufacturerItem entity
	private ManufacturerItem manufacturer;


	private Integer order = 0;
	private MultipartFile image = null;
	@NotNull
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private ProductImageItem productImage = null;


	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public ProductImageItem getProductImage() {
		return productImage;
	}

	public void setProductImage(ProductImageItem productImage) {
		this.productImage = productImage;
	}

	public ManufacturerItem getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(
			ManufacturerItem manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}


}

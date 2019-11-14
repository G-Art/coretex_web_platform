package com.coretex.shop.admin.model.catalog;

import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class Product implements Serializable {


	private static final long serialVersionUID = -4531526676134574984L;



	//provides wrapping to the main product entity
	@Valid
	private ProductItem product;


	@Valid
	private ProductAvailabilityItem availability = null;

	@Valid
	private ProductPriceItem price = null;

	private MultipartFile image = null;

	private ProductImageItem productImage = null;

	@NotEmpty
	private String productPrice = "0";

	private String dateAvailable;


	public String getDateAvailable() {
		return dateAvailable;
	}

	public void setDateAvailable(String dateAvailable) {
		this.dateAvailable = dateAvailable;
	}

	public ProductItem getProduct() {
		return product;
	}

	public void setProduct(ProductItem product) {
		this.product = product;
	}

	public void setAvailability(ProductAvailabilityItem availability) {
		this.availability = availability;
	}

	public ProductAvailabilityItem getAvailability() {
		return availability;
	}

	public void setPrice(ProductPriceItem price) {
		this.price = price;
	}

	public ProductPriceItem getPrice() {
		return price;
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


package com.coretex.shop.admin.model.digital;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.DigitalProductItem;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * A bean class responsible for getting form data from shop Admin for uploading
 * product files for a given product and validating the provided data.
 *
 * This will work as a wrapper for underlying cache where these content images will be stored
 * and retrieved in future.
 *
 * @author Carl Samson
 * @since 1.2
 *
 */
public class ProductFiles implements Serializable {

	private static final long serialVersionUID = 1L;


	private List<MultipartFile> file;
	private DigitalProductItem digitalProduct;
	private ProductItem product;

	@NotEmpty(message = "{product.files.invalid}")
	@Valid
	public List<MultipartFile> getFile() {
		return file;
	}

	public void setFile(final List<MultipartFile> file) {
		this.file = file;
	}


	public ProductItem getProduct() {
		return product;
	}

	public void setProduct(ProductItem product) {
		this.product = product;
	}

	public void setDigitalProduct(DigitalProductItem digitalProduct) {
		this.digitalProduct = digitalProduct;
	}

	public DigitalProductItem getDigitalProduct() {
		return digitalProduct;
	}


}

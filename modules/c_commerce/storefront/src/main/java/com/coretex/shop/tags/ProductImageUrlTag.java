package com.coretex.shop.tags;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.FilePathUtils;
import com.coretex.shop.utils.ImageFilePath;

public class ProductImageUrlTag extends RequestContextAwareTag {



	private static final long serialVersionUID = 6319855234657139862L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductImageUrlTag.class);


	private String imageName;
	private String imageType;
	private ProductItem product;

	@Resource
	private FilePathUtils filePathUtils;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	public int doStartTagInternal() throws JspException {
		try {


			if (filePathUtils == null || imageUtils == null) {
				WebApplicationContext wac = getRequestContext().getWebApplicationContext();
				AutowireCapableBeanFactory factory = wac.getAutowireCapableBeanFactory();
				factory.autowireBean(this);
			}

			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();

			MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			StringBuilder imagePath = new StringBuilder();

			String baseUrl = filePathUtils.buildRelativeStoreUri(request, merchantStore);
			imagePath.append(baseUrl);

			imagePath

					.append(imageUtils.buildProductImageUtils(merchantStore, product, this.getImageName())).toString();

			System.out.println("Printing image " + imagePath.toString());

			pageContext.getOut().print(imagePath.toString());


		} catch (Exception ex) {
			LOGGER.error("Error while getting content url", ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}


	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageType() {
		return imageType;
	}

	public void setProduct(ProductItem product) {
		this.product = product;
	}

	public ProductItem getProduct() {
		return product;
	}


}

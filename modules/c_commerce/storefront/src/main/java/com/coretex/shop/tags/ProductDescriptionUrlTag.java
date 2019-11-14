package com.coretex.shop.tags;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.FilePathUtils;


public class ProductDescriptionUrlTag extends RequestContextAwareTag {



	private static final long serialVersionUID = 6319855234657139862L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDescriptionUrlTag.class);


	private ProductItem product;

	@Resource
	private FilePathUtils filePathUtils;

	/**
	 * Created the product url for the store front
	 */
	public int doStartTagInternal() throws JspException {
		try {

			if (filePathUtils == null) {
				WebApplicationContext wac = getRequestContext().getWebApplicationContext();
				AutowireCapableBeanFactory factory = wac.getAutowireCapableBeanFactory();
				factory.autowireBean(this);
			}

			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();

			MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
			//*** IF USED FROM ADMIN THE STORE WILL BE NULL, THEN TRY TO USE ADMIN STORE
			if (merchantStore == null) {
				merchantStore = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
			}


			StringBuilder productPath = new StringBuilder();

			String baseUrl = filePathUtils.buildStoreUri(merchantStore, request);
			productPath.append(baseUrl);

			if (!StringUtils.isBlank(this.getProduct().getSeUrl())) {
				productPath.append(Constants.PRODUCT_URI).append("/");
				productPath.append(this.getProduct().getSeUrl());
			} else {
				productPath.append(Constants.PRODUCT_ID_URI).append("/");
				productPath.append(this.getProduct().getSku());
			}

			productPath.append(Constants.URL_EXTENSION);


			pageContext.getOut().print(productPath.toString());


		} catch (Exception ex) {
			LOGGER.error("Error while getting content url", ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	public ProductItem getProduct() {
		return product;
	}

	public void setProduct(ProductItem product) {
		this.product = product;
	}
}

package com.coretex.shop.tags;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.order.ReadableOrderProductDownload;
import com.coretex.shop.utils.FilePathUtils;


public class OrderProductDownloadUrlTag extends RequestContextAwareTag {



	private static final long serialVersionUID = 6319855234657139862L;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderProductDownloadUrlTag.class);


	private ReadableOrderProductDownload productDownload;

	private Long orderId;

	@Resource
	private FilePathUtils filePathUtils;


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

			StringBuilder filePath = new StringBuilder();

			filePath.append(filePathUtils.buildStoreUri(merchantStore, request));

			filePath
					.append(filePathUtils.buildOrderDownloadProductFilePath(merchantStore, this.getProductDownload(), this.getOrderId())).toString();


			pageContext.getOut().print(filePath.toString());


		} catch (Exception ex) {
			LOGGER.error("Error while getting order product download url", ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}


	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public ReadableOrderProductDownload getProductDownload() {
		return productDownload;
	}

	public void setProductDownload(ReadableOrderProductDownload productDownload) {
		this.productDownload = productDownload;
	}


}

package com.coretex.shop.tags;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.coretex.items.commerce_core_model.DigitalProductItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.FilePathUtils;


public class AdminProductDownloadUrlTag extends RequestContextAwareTag {



	private static final long serialVersionUID = 6319855234657139862L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminProductDownloadUrlTag.class);

	private DigitalProductItem digitalProduct;

	@Resource
	private FilePathUtils filePathUtils;


	public DigitalProductItem getDigitalProduct() {
		return digitalProduct;
	}

	public void setDigitalProduct(DigitalProductItem digitalProduct) {
		this.digitalProduct = digitalProduct;
	}

	public int doStartTagInternal() throws JspException {
		try {

			if (filePathUtils == null) {
				WebApplicationContext wac = getRequestContext().getWebApplicationContext();
				AutowireCapableBeanFactory factory = wac.getAutowireCapableBeanFactory();
				factory.autowireBean(this);
			}


			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();

			MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			HttpSession session = request.getSession();

			StringBuilder filePath = new StringBuilder();

			//TODO domain from merchant, else from global config, else from property (localhost)

			// example -> "/files/{storeCode}/{fileName}.{extension}"


			@SuppressWarnings("unchecked")
			Map<String, String> configurations = (Map<String, String>) session.getAttribute(Constants.STORE_CONFIGURATION);
			String scheme = Constants.HTTP_SCHEME;
			if (configurations != null) {
				scheme = configurations.get("scheme");
			}


			filePath.append(scheme).append("://")
					.append(merchantStore.getDomainName())
					//.append("/")
					.append(request.getContextPath());

			filePath
					.append(filePathUtils.buildAdminDownloadProductFilePath(merchantStore, digitalProduct)).toString();


			pageContext.getOut().print(filePath.toString());


		} catch (Exception ex) {
			LOGGER.error("Error while getting content url", ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}


}

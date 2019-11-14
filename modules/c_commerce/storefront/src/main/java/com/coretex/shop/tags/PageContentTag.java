package com.coretex.shop.tags;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.coretex.items.commerce_core_model.ContentItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.coretex.core.business.services.content.ContentService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.constants.Constants;


public class PageContentTag extends RequestContextAwareTag {



	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PageContentTag.class);


	@Resource
	private ContentService contentService;

	private String contentCode;


	public String getContentCode() {
		return contentCode;
	}


	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}


	@Override
	protected int doStartTagInternal() throws Exception {
		if (contentService == null || contentService == null) {
			LOGGER.debug("Autowiring contentService");
			WebApplicationContext wac = getRequestContext().getWebApplicationContext();
			AutowireCapableBeanFactory factory = wac.getAutowireCapableBeanFactory();
			factory.autowireBean(this);
		}

		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();

		LanguageItem language = (LanguageItem) request.getAttribute(Constants.LANGUAGE);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		ContentItem content = contentService.getByCode(contentCode, store, language);

		String pageContent = "";
		if (content != null) {

			pageContent = content.getDescription();
		}


		pageContext.getOut().print(pageContent);

		return SKIP_BODY;

	}


	public int doEndTag() {
		return EVAL_PAGE;
	}


}

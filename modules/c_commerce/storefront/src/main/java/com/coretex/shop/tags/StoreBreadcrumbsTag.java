package com.coretex.shop.tags;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.shop.Breadcrumb;


public class StoreBreadcrumbsTag extends TagSupport {



	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(StoreBreadcrumbsTag.class);

	private UUID categoryId = null;
	private UUID productId = null;

	public int doStartTag() throws JspException {
		try {


			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();

			Breadcrumb breadCrumb = (Breadcrumb) request.getAttribute(Constants.BREADCRUMB);

			StringBuilder ref = new StringBuilder();

			if (breadCrumb != null && !StringUtils.isBlank(breadCrumb.getUrlRefContent())) {
				ref.append(Constants.SLASH).append(Constants.REF).append(Constants.EQUALS).append(breadCrumb.getUrlRefContent());
				if (categoryId != null) {
					List<String> ids = this.parseBreadCrumb(breadCrumb.getUrlRefContent());
					if (!ids.contains(String.valueOf(this.getCategoryId()))) {
						ref.append(",").append(this.getCategoryId());
					}
				}
			} else {
				if (categoryId != null) {
					ref.append(Constants.SLASH).append(Constants.REF).append(Constants.EQUALS).append(Constants.REF_C).append(this.getCategoryId());
				} else {
				}
			}


			pageContext.getOut().print(ref.toString());


		} catch (Exception ex) {
			LOGGER.error("Error while getting content url", ex);
		}
		return SKIP_BODY;
	}

	/**
	 * only category
	 **/
	private List<String> parseBreadCrumb(String refContent) throws Exception {

		/** c:1,2,3 **/
		String[] categoryComa = refContent.split(":");
		String[] categoryIds = categoryComa[1].split(",");
		return new LinkedList(Arrays.asList(categoryIds));


	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	public UUID getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(UUID categoryId) {
		this.categoryId = categoryId;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}


}

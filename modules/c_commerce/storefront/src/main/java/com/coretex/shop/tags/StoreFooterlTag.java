package com.coretex.shop.tags;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.constants.Constants;

public class StoreFooterlTag extends TagSupport {



	private static final long serialVersionUID = 6319855234657139862L;
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreFooterlTag.class);

	private final static String COPY = "\u00a9";


	public int doStartTag() throws JspException {
		try {


			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();

			MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);


			StringBuilder y = new StringBuilder();
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);


			if (merchantStore != null) {
				Date dateBusiness = merchantStore.getInBusinessSince();
				if (dateBusiness != null) {
					Calendar c = Calendar.getInstance();
					c.setTime(dateBusiness);
					int startBusiness = c.get(Calendar.YEAR);
					if (startBusiness < currentYear) {
						y.append(startBusiness).append("-");
					}
				}
			}

			y.append(currentYear);

			StringBuilder copy = new StringBuilder();
			copy.append(COPY).append(" ").append(merchantStore.getStoreName()).append(" ").append(y.toString());

			pageContext.getOut().print(copy.toString());


		} catch (Exception ex) {
			LOGGER.error("Error while getting content url", ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}


}

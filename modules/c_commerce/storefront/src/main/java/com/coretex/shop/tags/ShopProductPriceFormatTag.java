package com.coretex.shop.tags;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.cx_core.CurrencyItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.utils.ProductPriceUtils;
import com.coretex.shop.constants.Constants;


public class ShopProductPriceFormatTag extends RequestContextAwareTag {



	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ShopProductPriceFormatTag.class);

	@Resource
	private PricingService pricingService;

	@Resource
	private ProductPriceUtils productPriceUtils;


	private BigDecimal value;


	private CurrencyItem currency;


	public CurrencyItem getCurrency() {
		return currency;
	}


	public void setCurrency(CurrencyItem currency) {
		this.currency = currency;
	}


	@Override
	protected int doStartTagInternal() throws Exception {
		if (pricingService == null || productPriceUtils == null) {
			LOGGER.debug("Autowiring productPriceUtils");
			WebApplicationContext wac = getRequestContext().getWebApplicationContext();
			AutowireCapableBeanFactory factory = wac.getAutowireCapableBeanFactory();
			factory.autowireBean(this);
		}

		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		String formatedPrice = null;

		if (this.getCurrency() != null) {
			formatedPrice = productPriceUtils.getFormatedAmountWithCurrency(this.getCurrency(), this.getValue());
		} else {
			formatedPrice = pricingService.getDisplayAmount(this.getValue(), store);
		}

		pageContext.getOut().print(formatedPrice);

		return SKIP_BODY;

	}


	public int doEndTag() {
		return EVAL_PAGE;
	}


	public void setValue(BigDecimal value) {
		this.value = value;
	}


	public BigDecimal getValue() {
		return value;
	}


}

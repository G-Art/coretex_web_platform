package com.coretex.core.business.services.catalog.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.utils.ProductPriceUtils;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.CurrencyItem;

/**
 * Contains all the logic required to calculate product price
 *
 * @author Carl Samson
 */
@Service("pricingService")
public class PricingServiceImpl implements PricingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingServiceImpl.class);


	@Resource
	private ProductPriceUtils priceUtil;

	@Override
	public FinalPrice calculateProductPrice(ProductItem product) {
		return priceUtil.getFinalPrice(product);
	}

	@Override
	public FinalPrice calculateProductPrice(ProductItem product, CustomerItem customer) {
		/** TODO add rules for price calculation **/
		return priceUtil.getFinalPrice(product);
	}

	@Override
	public FinalPrice calculateProductPrice(ProductItem product, List<ProductAttributeItem> attributes) {
		return priceUtil.getFinalProductPrice(product, attributes);
	}

	@Override
	public FinalPrice calculateProductPrice(ProductItem product, List<ProductAttributeItem> attributes, CustomerItem customer) {
		/** TODO add rules for price calculation **/
		return priceUtil.getFinalProductPrice(product, attributes);
	}

	@Override
	public BigDecimal calculatePriceQuantity(BigDecimal price, int quantity) {
		return price.multiply(new BigDecimal(quantity));
	}

	@Override
	public String getDisplayAmount(BigDecimal amount, MerchantStoreItem store) throws ServiceException {
		try {
			String price = priceUtil.getStoreFormatedAmountWithCurrency(store, amount);
			return price;
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount.toString());
			throw new ServiceException(e);
		}
	}

	@Override
	public String getDisplayAmount(BigDecimal amount, Locale locale,
								   CurrencyItem currency, MerchantStoreItem store) throws ServiceException {
		try {
			String price = priceUtil.getFormatedAmountWithCurrency(locale, currency, amount);
			return price;
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amunt " + amount.toString() + " using locale " + locale.toString() + " and currency " + currency.toString());
			throw new ServiceException(e);
		}
	}

	@Override
	public String getStringAmount(BigDecimal amount, MerchantStoreItem store)
			throws ServiceException {
		try {
			String price = priceUtil.getAdminFormatedAmount(amount);
			return price;
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount.toString());
			throw new ServiceException(e);
		}
	}

	@Override
	public BigDecimal getAmount(String amount) throws ServiceException {

		try {
			return priceUtil.getAmount(amount);
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount);
			throw new ServiceException(e);
		}

	}


}

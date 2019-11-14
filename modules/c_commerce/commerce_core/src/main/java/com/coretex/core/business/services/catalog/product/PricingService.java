package com.coretex.core.business.services.catalog.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.CurrencyItem;


/**
 * Services for ProductItem item price calculation.
 *
 * @author Carl Samson
 */
public interface PricingService {

	/**
	 * Calculates the FinalPrice of a ProductItem taking into account
	 * all defined prices and possible rebates
	 *
	 * @param product
	 * @return
	 * @throws ServiceException
	 */
	FinalPrice calculateProductPrice(ProductItem product);

	/**
	 * Calculates the FinalPrice of a ProductItem taking into account
	 * all defined prices and possible rebates. It also applies other calculation
	 * based on the customer
	 *
	 * @param product
	 * @param customer
	 * @return
	 * @throws ServiceException
	 */
	FinalPrice calculateProductPrice(ProductItem product, CustomerItem customer)
			throws ServiceException;

	/**
	 * Calculates the FinalPrice of a ProductItem taking into account
	 * all defined prices and possible rebates. This method should be used to calculate
	 * any additional prices based on the default attributes or based on the user selected attributes.
	 *
	 * @param product
	 * @param attributes
	 * @return
	 * @throws ServiceException
	 */
	FinalPrice calculateProductPrice(ProductItem product,
									 List<ProductAttributeItem> attributes) throws ServiceException;

	/**
	 * Calculates the FinalPrice of a ProductItem taking into account
	 * all defined prices and possible rebates. This method should be used to calculate
	 * any additional prices based on the default attributes or based on the user selected attributes.
	 * It also applies other calculation based on the customer
	 *
	 * @param product
	 * @param attributes
	 * @param customer
	 * @return
	 * @throws ServiceException
	 */
	FinalPrice calculateProductPrice(ProductItem product,
									 List<ProductAttributeItem> attributes, CustomerItem customer)
			throws ServiceException;

	/**
	 * Method to be used to print a displayable formated amount to the end user
	 *
	 * @param amount
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	String getDisplayAmount(BigDecimal amount, MerchantStoreItem store)
			throws ServiceException;

	/**
	 * Method to be used when building an amount formatted with the appropriate currency
	 *
	 * @param amount
	 * @param locale
	 * @param currency
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	String getDisplayAmount(BigDecimal amount, Locale locale, CurrencyItem currency, MerchantStoreItem store)
			throws ServiceException;

	/**
	 * Converts a String amount to BigDecimal
	 * Takes care of String amount validation
	 *
	 * @param amount
	 * @return
	 * @throws ServiceException
	 */
	BigDecimal getAmount(String amount) throws ServiceException;

	/**
	 * String format of the money amount without currency symbol
	 *
	 * @param amount
	 * @param store
	 * @return
	 * @throws ServiceException
	 */
	String getStringAmount(BigDecimal amount, MerchantStoreItem store)
			throws ServiceException;

	/**
	 * Method for calculating sub total
	 *
	 * @param price
	 * @param quantity
	 * @return
	 */
	BigDecimal calculatePriceQuantity(BigDecimal price, int quantity);
}

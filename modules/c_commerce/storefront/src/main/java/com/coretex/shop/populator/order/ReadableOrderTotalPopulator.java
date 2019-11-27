package com.coretex.shop.populator.order;

import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.order.total.ReadableOrderTotal;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class ReadableOrderTotalPopulator extends
		AbstractDataPopulator<OrderTotalItem, ReadableOrderTotal> {


	private PricingService pricingService;


	private LabelUtils messages;


	@Override
	public ReadableOrderTotal populate(OrderTotalItem source,
									   ReadableOrderTotal target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {

		Validate.notNull(pricingService, "PricingService must be set");
		Validate.notNull(messages, "LabelUtils must be set");

		Locale locale = LocaleUtils.getLocale(language);

		try {

			target.setCode(source.getOrderTotalCode());
			target.setUuid(source.getUuid());
			target.setModule(source.getModule());
			target.setOrder(source.getSortOrder());


			target.setTitle(messages.getMessage(source.getOrderTotalCode(), locale, source.getOrderTotalCode()));
			target.setText(source.getText());

			target.setValue(source.getValue());
			target.setTotal(pricingService.getDisplayAmount(source.getValue(), store));

			if (!StringUtils.isBlank(source.getOrderTotalCode())) {
				if (Constants.OT_DISCOUNT_TITLE.equals(source.getOrderTotalCode())) {
					target.setDiscounted(true);
				}
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}

		return target;

	}

	@Override
	protected ReadableOrderTotal createTarget() {
		return new ReadableOrderTotal();
	}

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

	public LabelUtils getMessages() {
		return messages;
	}

	public void setMessages(LabelUtils messages) {
		this.messages = messages;
	}

}
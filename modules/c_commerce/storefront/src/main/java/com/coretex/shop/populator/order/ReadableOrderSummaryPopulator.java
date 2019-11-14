package com.coretex.shop.populator.order;

import com.coretex.items.commerce_core_model.OrderTotalItem;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.order.ReadableOrderTotalSummary;
import com.coretex.shop.model.order.total.ReadableOrderTotal;
import com.coretex.shop.utils.LabelUtils;

public class ReadableOrderSummaryPopulator extends AbstractDataPopulator<OrderTotalSummary, ReadableOrderTotalSummary> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReadableOrderSummaryPopulator.class);

	private PricingService pricingService;

	private LabelUtils messages;


	@Override
	public ReadableOrderTotalSummary populate(OrderTotalSummary source, ReadableOrderTotalSummary target,
											  MerchantStoreItem store, LanguageItem language) throws ConversionException {

		Validate.notNull(pricingService, "PricingService must be set");
		Validate.notNull(messages, "LabelUtils must be set");

		if (target == null) {
			target = new ReadableOrderTotalSummary();
		}

		try {

			if (source.getSubTotal() != null) {
				target.setSubTotal(pricingService.getDisplayAmount(source.getSubTotal(), store));
			}
			if (source.getTaxTotal() != null) {
				target.setTaxTotal(pricingService.getDisplayAmount(source.getTaxTotal(), store));
			}
			if (source.getTotal() != null) {
				target.setTotal(pricingService.getDisplayAmount(source.getTotal(), store));
			}

			if (!CollectionUtils.isEmpty(source.getTotals())) {
				ReadableOrderTotalPopulator orderTotalPopulator = new ReadableOrderTotalPopulator();
				orderTotalPopulator.setMessages(messages);
				orderTotalPopulator.setPricingService(pricingService);
				for (OrderTotalItem orderTotal : source.getTotals()) {
					ReadableOrderTotal t = new ReadableOrderTotal();
					orderTotalPopulator.populate(orderTotal, t, store, language);
					target.getTotals().add(t);
				}
			}


		} catch (Exception e) {
			LOGGER.error("Error during amount formatting " + e.getMessage());
			throw new ConversionException(e);
		}

		return target;

	}

	@Override
	protected ReadableOrderTotalSummary createTarget() {
		// TODO Auto-generated method stub
		return null;
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

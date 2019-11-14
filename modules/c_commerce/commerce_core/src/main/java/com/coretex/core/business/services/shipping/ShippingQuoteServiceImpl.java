package com.coretex.core.business.services.shipping;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.QuoteItem;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.shipping.ShippingQuoteDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.model.shipping.ShippingSummary;

@Service("shippingQuoteService")
public class ShippingQuoteServiceImpl extends SalesManagerEntityServiceImpl<QuoteItem> implements ShippingQuoteService {


	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingQuoteServiceImpl.class);

	private ShippingQuoteDao shippingQuoteDao;

	@Resource
	private ShippingService shippingService;

	public ShippingQuoteServiceImpl(ShippingQuoteDao repository) {
		super(repository);
		this.shippingQuoteDao = repository;
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<QuoteItem> findByOrder(OrderItem order) throws ServiceException {
		Validate.notNull(order, "OrderItem cannot be null");
		return this.shippingQuoteDao.findByOrder(order.getUuid());
	}

	@Override
	public ShippingSummary getShippingSummary(UUID quoteId, MerchantStoreItem store) throws ServiceException {

		Validate.notNull(quoteId, "quoteId must not be null");

		QuoteItem q = shippingQuoteDao.find(quoteId);

		ShippingSummary quote = null;

		if (q != null) {

			quote = new ShippingSummary();
			quote.setDeliveryAddress(q.getDelivery());
			quote.setShipping(q.getPrice());
			quote.setShippingModule(q.getModule());
			quote.setShippingOption(q.getOptionName());
			quote.setShippingOptionCode(q.getOptionCode());
			quote.setHandling(q.getHandling());

			if (shippingService.hasTaxOnShipping(store)) {
				quote.setTaxOnShipping(true);
			}


		}


		return quote;

	}


}

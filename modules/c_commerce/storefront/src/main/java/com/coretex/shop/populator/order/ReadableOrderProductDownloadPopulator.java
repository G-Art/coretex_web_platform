package com.coretex.shop.populator.order;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderProductDownloadItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.shop.model.order.ReadableOrderProductDownload;

public class ReadableOrderProductDownloadPopulator extends
		AbstractDataPopulator<OrderProductDownloadItem, ReadableOrderProductDownload> {

	@Override
	public ReadableOrderProductDownload populate(OrderProductDownloadItem source,
												 ReadableOrderProductDownload target, MerchantStoreItem store,
												 LocaleItem language) throws ConversionException {
		try {

			target.setProductName(source.getOrderProduct().getProductName());
			target.setDownloadCount(source.getDownloadCount());
			target.setDownloadExpiryDays(source.getMaxdays());
			target.setUuid(source.getUuid());
			target.setFileName(source.getOrderProductFilename());
			target.setOrderId(source.getOrderProduct().getOrder().getUuid());

			return target;

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	@Override
	protected ReadableOrderProductDownload createTarget() {
		return new ReadableOrderProductDownload();
	}


}

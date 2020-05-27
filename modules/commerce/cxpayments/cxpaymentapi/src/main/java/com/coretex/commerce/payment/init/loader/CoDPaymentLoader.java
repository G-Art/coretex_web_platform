package com.coretex.commerce.payment.init.loader;

import com.coretex.commerce.core.init.loader.DataLoader;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.items.cxpaymentapi.CODPaymentModeItem;
import com.coretex.items.cxpaymentapi.PaymentModeItem;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

@Component
public class CoDPaymentLoader implements DataLoader {

	@Resource
	private ItemService itemService;

	private static final Logger LOGGER = LoggerFactory.getLogger(CoDPaymentLoader.class);

	@Override
	public int priority() {
		return PRIORITY_10;
	}

	@Override
	public boolean parallel() {
		return true;
	}

	@Override
	public boolean load(String name) {
		LOGGER.info(String.format("%s : Populating Cache on Delivery payment ", name));;
		PaymentModeItem paymentMode = new CODPaymentModeItem();
		paymentMode.setCode("COD");
		paymentMode.setName("Pay on delivery", Locale.ENGLISH);
		paymentMode.setName("Оплата при доставке (наложеный)", LocaleUtils.toLocale("ru"));
		paymentMode.setName("Оплата при доставці (наложений)", LocaleUtils.toLocale("ua"));
		paymentMode.setActive(true);
		itemService.save(paymentMode);
		return true;
	}


}
